import * as schema from "@/server/db/schema";
import { Client } from "@planetscale/database";
import csvParser from "csv-parser";
import * as dotenv from "dotenv";
import { drizzle } from "drizzle-orm/planetscale-serverless";
import fs from "fs";
import { z } from "zod";
dotenv.config({ path: "./.env" });

if (!("DATABASE_URL" in process.env))
  throw new Error("DATABASE_URL not found on .env.development");

const comicSchema = z.object({
  Series: z.string(),
  Issue: z.string().optional(),
  "Full Title": z.string().optional(),
  "Variant Description": z.string(),
  Publisher: z.string().optional(),
  "Release Date": z.string().optional(),
  Format: z.string(),
  "Added Date": z.string(),
  Creators: z.string(),
  Volume: z.string().optional(),
});

function getComics(): Promise<z.infer<typeof comicSchema>[]> {
  const data = [] as z.infer<typeof comicSchema>[];

  return new Promise((resolve, reject) => {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    fs.createReadStream("./data/comics.csv")
      // eslint-disable-next-line @typescript-eslint/no-unsafe-call
      .pipe(csvParser())
      // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
      .on("data", (row) => {
        const parsedRow = comicSchema.parse(row);

        // Split the Series field into series and volume
        const [series, volume = "1"] = parsedRow.Series.split("Vol.");

        // Update the Series and Volume fields
        parsedRow.Series = series!.trim();
        parsedRow.Volume = volume.trim();

        data.push(parsedRow);
      })
      // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
      .on("end", () => {
        resolve(data);
      })
      // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
      .on("error", (e) => {
        reject(e);
      });
  });
}

async function main() {
  const db = drizzle(
    new Client({
      url: process.env.DATABASE_URL,
    }).connection(),
    { schema },
  );
  const comicsParse = await getComics();

  const comicData = comicsParse
    .map((comic) => ({
      seriesTitle: comic.Series.trim(),
      volumeNumber: comic.Volume!.trim(),
      issueNumber: comic.Issue?.trim(),
      storyTitle: comic["Full Title"]?.trim(),
      publisher: comic.Publisher?.trim(),
      publicationDate:
        comic["Release Date"] && !isNaN(Date.parse(comic["Release Date"]))
          ? new Date(comic["Release Date"])
          : undefined, // Set to undefined if Release Date is invalid
      creators: comic.Creators.trim(),
    }))
    .filter(
      (
        comic,
      ): comic is {
        seriesTitle: string;
        volumeNumber: string;
        issueNumber: string;
        storyTitle: string;
        publisher: string;
        publicationDate: Date;
        creators: string;
      } =>
        comic.publicationDate !== undefined &&
        comic.publisher !== undefined &&
        comic.issueNumber !== undefined,
    );

  const midpoint = Math.floor(comicData.length / 2);
  const firstHalf = comicData.slice(0, midpoint);
  const secondHalf = comicData.slice(midpoint);
  const firstHalfStripped = firstHalf.map(({ creators, ...comic }) => comic);
  const secondHalfStripped = secondHalf.map(({ creators, ...comic }) => comic);

  try {
    await db.insert(schema.comics).values(firstHalfStripped);
  } catch (e) {
    console.log("e", e);
  }

  try {
    await db.insert(schema.comics).values(secondHalfStripped);
  } catch (e) {
    console.log("e", e);
  }

  console.log("Done seeding comics");

  // create or connect creators to comics

  const creators = comicsParse
    .map((comic) => {
      const creators = comic.Creators.split("|").map((creator) =>
        creator.trim(),
      );

      return creators;
    })
    .flat();

  const uniqueCreators = [...new Set(creators)];

  const creatorData = uniqueCreators.map((creator) => ({ name: creator }));

  try {
    await db.insert(schema.creators).values(creatorData);
  } catch (e) {
    console.log("e", e);
  }

  console.log("Done seeding creators");

  const createdComics = await db.select().from(schema.comics);
  const createdCreators = await db.select().from(schema.creators);

  const comicsWithCreators = comicData.map((comic) => {
    const comicId = createdComics.find(
      (createdComic) =>
        createdComic.publisher === comic.publisher &&
        createdComic.seriesTitle === comic.seriesTitle &&
        createdComic.volumeNumber === comic.volumeNumber &&
        createdComic.issueNumber === comic.issueNumber &&
        createdComic.publicationDate.getTime() ===
          comic.publicationDate.getTime(),
    )?.id;

    const creatorIds = comic.creators.split("|").map((creator) => {
      const trimmedCreator = creator.trim();
      const foundCreator = createdCreators.find(
        (createdCreator) => createdCreator.name === trimmedCreator,
      );

      return foundCreator ? foundCreator.id : undefined;
    });

    return { comicId, creatorIds };
  });

  // take the comicId and the array or creatorIds and convert it into an array of objects
  // with comicId and creatorId
  const comicsWithCreatorsFlat = comicsWithCreators
    .map(({ comicId, creatorIds }) => {
      return creatorIds.map((creatorId) => ({
        comicId,
        comicCreatorId: creatorId,
      }));
    })
    .flat()
    .filter(
      (
        comicWithCreator,
      ): comicWithCreator is {
        comicId: number;
        comicCreatorId: number;
      } =>
        comicWithCreator.comicCreatorId !== undefined &&
        comicWithCreator.comicId !== undefined,
    );

  try {
    await db.insert(schema.comicsToCreators).values(comicsWithCreatorsFlat);
  } catch (e) {
    console.log("e", e);
  }

  console.log("Done seeding comics to creators");
}

main();
