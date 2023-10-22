import * as z from "zod";

export const comicSchema = z.object({
  publisher: z.string().min(1),
  seriesTitle: z.string().min(1),
  volumeNumber: z.number().int().positive(),
  issueNumber: z.number().int().positive(),
  publicationDate: z.string().min(1),
  storyTitle: z.string().min(1).nullable(),
  creators: z
    .object({
      name: z.string().min(1),
    })
    .array()
    .optional(),
  principalCharacters: z
    .object({
      name: z.string().min(1),
    })
    .array()
    .optional(),
  description: z.string().min(1).nullable(),
  id: z.number().int().positive().nullable(),
});
