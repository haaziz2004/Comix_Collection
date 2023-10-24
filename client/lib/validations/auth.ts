import * as z from "zod";

export const userAuthSchema = z.object({
  username: z
    .string()
    .min(1, "Please enter a username.")
    .max(32, "Username must be less than 32 characters."),
  password: z
    .string()
    .min(1, "Please enter a password.")
    .max(255, "Password must be less than 255 characters."),
});
