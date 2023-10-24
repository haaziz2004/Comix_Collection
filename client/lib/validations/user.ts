import * as z from "zod";
import { comicSchema } from "./comic";
import { userAuthSchema } from "./auth";

export const userSchema = userAuthSchema.merge(
  z.object({
    id: z.number().int().positive(),
    role: z.enum(["USER", "ADMIN"]),
    userComics: comicSchema.array(),
  }),
);
