import * as z from "zod";

export const comicSchema = z.object({
  publisher: z.string().min(1),
  seriesTitle: z.string().min(1),
  volumeNumber: z.string().min(1),
  issueNumber: z.string().min(1),
  publicationDate: z.string().min(1),
  storyTitle: z.string().min(1).nullable(),
  creators: z.array(
    z.object({
      name: z.string().min(1),
    }),
  ),
  principleCharacters: z.array(
    z.object({
      name: z.string().min(1),
    }),
  ),
  description: z.string().min(1).nullable(),
  id: z.number().int().positive(),
  value: z.number().nonnegative().nullable(),
  grade: z.number().int().nonnegative().nullable(),
  slabbed: z.boolean().nullable(),
});
