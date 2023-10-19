import { db } from "@/server/db";
import csvParser from "csv-parser";
import fs from "fs";

export async function GET() {
  // const creators = new Set<{
  //   name: string;
  // }>();
  let results = [] as {
    Series: string;
    Issue: string;
    "Full Title": string;
    "Variant Description": string;
    Publisher: string;
    "Release Date": string;
    Format: string;
    "Added Date": string;
    Creators: string | string[];
    Volume: string;
  }[];
  fs.createReadStream("./data/comics.csv")
    .pipe(csvParser())
    .on("data", (data) => results.push(data))
    // eslint-disable-next-line @typescript-eslint/no-misused-promises
    .on("end", async () => {
      // loop through results and split the Creators field into an array delimited by |
      // trim whitespace from each element
      results.forEach((comic) => {
        if (typeof comic.Creators === "string") {
          comic.Creators = comic.Creators.split("|").map((creator) => {
            // creators.add({ name: creator.trim() });
            return creator.trim();
          });
        }
        if (comic.Series.includes(", Vol. ")) {
          const split = comic.Series.split(", Vol. ");
          comic.Series = split[0]?.trim();
          comic.Volume = split[1]?.trim();
        } else {
          comic.Volume = "1";
        }
      });

      // filter out comics that are missing required fields
      results = results.filter((comic) => {
        if (
          comic.Series === "" ||
          comic.Publisher === "" ||
          comic.Issue === "" ||
          comic["Release Date"] === ""
        ) {
          return false;
        }
        return true;
      });

      console.log("Total comics in csv: ", results.length);

      // find any duplicate comics i.e. same series, volume, issue, and publication date
      const duplicates = results.filter((comic, index) => {
        const matchIndex = results.findIndex((c, i) => {
          if (
            i > index &&
            c.Series === comic.Series &&
            c.Volume === comic.Volume &&
            c.Issue === comic.Issue &&
            c["Release Date"] === comic["Release Date"]
          ) {
            return true;
          }
          return false;
        });
        if (matchIndex !== -1) {
          return true;
        }
        return false;
      });

      console.log("Duplicate comics: ", duplicates.length);

      // save the duplicates to a file

      function reduceCreators(creators: string[]): string {
        return creators.reduce((prev, curr) => {
          if (prev === "") {
            return curr;
          }
          return `${prev} | ${curr}`;
        }, "");
      }

      const duplicatesFile = fs.createWriteStream("./data/duplicates.csv");
      duplicatesFile.write(
        "Series,Issue,Full Title,Variant Description,Publisher,Release Date,Format,Added Date,Creators,Volume\n",
      );
      duplicates.forEach((comic) => {
        duplicatesFile.write(
          `"${comic.Series}","${comic.Issue}","${comic["Full Title"]}","${
            comic["Variant Description"]
          }","${comic.Publisher}","${comic["Release Date"]}","${
            comic.Format
          }","${comic["Added Date"]}","${reduceCreators(comic.Creators)}","${
            comic.Volume
          }"\n`,
        );
      });

      // const comics = await db.comic.findMany({
      //   select: {
      //     seriesTitle: true,
      //     issueNumber: true,
      //     publicationDate: true,
      //     publisher: true,
      //     volumeNumber: true,
      //   },
      // });

      // console.log("Current comics in database: ", comics.length);

      // find which comics are missing from the database
      // const missingComics = results.filter((comic) => {
      //   const match = comics.find((c) => {
      //     if (
      //       c.seriesTitle === comic.Series &&
      //       c.issueNumber === comic.Issue &&
      //       c.publicationDate === comic["Release Date"] &&
      //       c.publisher === comic.Publisher &&
      //       c.volumeNumber === comic.Volume
      //     ) {
      //       return true;
      //     }
      //     return false;
      //   });
      //   if (match) {
      //     return false;
      //   }
      //   return true;
      // });

      // console.log("Missing comics: ", missingComics.length);

      // const batchSize = 100;

      // for (let i = 0; i < missingComics.length; i += batchSize) {
      //   const batch = missingComics.slice(i, i + batchSize);
      //   const promises = batch.map(async (comic) => {
      //     try {
      //       await db.comic.create({
      //         data: {
      //           seriesTitle: comic.Series,
      //           issueNumber: comic.Issue,
      //           storyTitle:
      //             comic["Full Title"] === "" ? null : comic["Full Title"],
      //           description:
      //             comic["Variant Description"] === ""
      //               ? null
      //               : comic["Variant Description"],
      //           publisher: comic.Publisher,
      //           publicationDate: comic["Release Date"],

      //           volumeNumber: comic.Volume,
      //           creators: {
      //             connect: comic.Creators.map((creator) => {
      //               return { name: creator };
      //             }),
      //           },
      //         },
      //       });
      //     } catch (error) {
      //       console.error(`Error creating comic: ${error.message}`);
      //       throw error;
      //     }
      //   });
      //   await Promise.allSettled(promises);
      // }

      // console.log("Total comics in csv: ", results.length);
      // console.log("Current comics in database: ", comics.length);
      // console.log("Missing comics: ", missingComics.length);

      // find which comics are in results but not in the database
      // const missingComics = results.filter((comic) => {
      //   const match = comics.find((c) => {
      //     if (
      //       c.seriesTitle === comic.Series &&
      //       c.issueNumber === comic.Issue &&
      //       c.publicationDate === comic["Release Date"] &&
      //       c.publisher === comic.Publisher
      //     ) {
      //       return true;
      //     }
      //     return false;
      //   });
      //   if (match) {
      //     return false;
      //   }
      //   return true;
      // });

      // console.log(missingComics.length);
      // console.log(missingComics);

      // const batchSize = 100;
      // for (let i = 0; i < results.length; i += batchSize) {
      //   const batch = results.slice(i, i + batchSize);
      //   const promises = batch.map(async (comic) => {
      //     try {
      //       await db.comic.create({
      //         data: {
      //           seriesTitle: comic.Series,
      //           issueNumber: comic.Issue,
      //           storyTitle:
      //             comic["Full Title"] === "" ? null : comic["Full Title"],
      //           description:
      //             comic["Variant Description"] === ""
      //               ? null
      //               : comic["Variant Description"],
      //           publisher: comic.Publisher,
      //           publicationDate: comic["Release Date"],

      //           volumeNumber: comic.Volume,
      //           creators: {
      //             connect: comic.Creators.map((creator) => {
      //               return { name: creator };
      //             }),
      //           },
      //         },
      //       });
      //     } catch (error) {
      //       console.error(`Error creating comic: ${error.message}`);
      //       throw error;
      //     }
      //   });
      //   await Promise.allSettled(promises);
      // }
    });
  return new Response(JSON.stringify(results));
}
