import * as z from "zod";

// export const collection = z.object({
//   elements: z.array(z.lazy(() => collectionElement)),
//   value: z.number(),
//   issueNumber: z.string(),
//   publisher: z.string(),
//   volumeNumber: z.string(),
//   seriesTitle: z.string(),
//   numberOfIssues: z.number(),
// })

// const collectionElement = z.array(z.object({
//   elements:
//   numberOfIssues: z.number(),
//   id: z.number(),
//   publisher: z.string(),
//   seriesTitle: z.string(),
//   volumeNumber: z.string(),
//   issueNumber: z.string(),
//   publicationDate: z.string(),
//   sotryTitle: z.string().nullable(),
//   creators: z.array(z.object({
//     name: z.string(),
//   })).optional().nullable(),
//   principleCharacters: z.array(z.object({
//     name: z.string(),
//   })).optional().nullable(),
//   description: z.string().nullable(),
//   value: z.number(),
//   grade: z.number(),
//   slabbed: z.boolean(),
// })).nullable()

const comicCreator = z.object({
  name: z.string(),
});

const comic = z.object({
  numberOfIssues: z.number(),
  id: z.number(),
  publisher: z.string(),
  seriesTitle: z.string(),
  volumeNumber: z.string(),
  issueNumber: z.string(),
  publicationDate: z.string(),
  storyTitle: z.string().nullable(),
  creators: z.array(comicCreator).optional().nullable(),
  principleCharacters: z
    .array(
      z.object({
        name: z.string(),
      }),
    )
    .optional()
    .nullable(),
  description: z.string().nullable(),
  value: z.number(),
  grade: z.number(),
  slabbed: z.boolean(),
});

const collectionElement = z.object({
  elements: z
    .lazy(() => collectionElement)
    .optional()
    .nullable(),
  numberOfIssues: z.number(),
  id: z.number(),
  publisher: z.string(),
  seriesTitle: z.string(),
  volumeNumber: z.string(),
  issueNumber: z.string(),
  publicationDate: z.string(),
  storyTitle: z.string().nullable(),
  creators: z.array(comicCreator).optional().nullable(),
  principleCharacters: z
    .array(
      z.object({
        name: z.string(),
      }),
    )
    .optional()
    .nullable(),
  description: z.string().nullable(),
  value: z.number(),
  grade: z.number(),
  slabbed: z.boolean(),
});

export const collection = z.object({
  elements: z.array(collectionElement),
  value: z.number(),
  issueNumber: z.string(),
  publisher: z.string(),
  volumeNumber: z.string(),
  seriesTitle: z.string(),
  numberOfIssues: z.number(),
});
