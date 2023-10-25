import * as z from "zod";

export const comicSchema = z.object({
  publisher: z.string().min(1),
  seriesTitle: z.string().min(1),
  volumeNumber: z.string().min(1),
  issueNumber: z.string().min(1),
  publicationDate: z.string().min(1),
  storyTitle: z.string().min(1).nullable(),
  creators: z
    .array(
      z.object({
        name: z.string().min(1),
      }),
    )
    .nullable()
    .optional(),
  principleCharacters: z
    .array(
      z.object({
        name: z.string().min(1),
      }),
    )
    .nullable()
    .optional(),
  description: z.string().min(1).nullable(),
  id: z.number().int().positive(),
  value: z.number().nonnegative().nullable(),
  grade: z.number().int().nonnegative().nullable(),
  slabbed: z.boolean().nullable(),
});

export const comicEditSchema = z.object({
  publisher: z.string().min(1).nullable().optional(),
  seriesTitle: z.string().min(1).nullable().optional(),
  volumeNumber: z.string().min(1).nullable().optional(),
  issueNumber: z.string().min(1).nullable().optional(),
  publicationDate: z.string().min(1).nullable().optional(),
  storyTitle: z.string().nullable().optional(),
  creators: z.string().nullable().optional(),
  principleCharacters: z.string().nullable().optional(),
  description: z.string().nullable().optional(),
  // value: z.coerce.number().nonnegative().nullable().optional(),
  // grade: z.coerce.number().int().nonnegative().nullable().optional(),
  // slabbed: z.coerce.boolean().nullable().optional(),
  value: z.string().nullable().optional(),
  grade: z.string().nullable().optional(),
  slabbed: z.string().nullable().optional(),
});
