import { z } from "zod";

export const getAllSchema = z.array(
  z.object({
    id: z.number(),
    publisher: z.string(),
    seriesTitle: z.string(),
    volumeNumber: z.string(),
    issueNumber: z.string(),
    publicationDate: z.coerce.date(),
    storyTitle: z.string(),
    description: z.string().nullable(),
  }),
);

export const getByIdSchema = z.object({
  id: z.number(),
  publisher: z.string(),
  seriesTitle: z.string(),
  volumeNumber: z.string(),
  issueNumber: z.string(),
  publicationDate: z.coerce.date(),
  storyTitle: z.string(),
  description: z.string().nullable(),
  creators: z.array(
    z.object({
      name: z.string(),
    }),
  ),
});

export const loginSchema = z.object({
  id: z.number(),
  username: z.string(),
  password: z.string(),
  role: z.enum(["ADMIN", "USER"]),
});

export const registerSchema = z.number();
