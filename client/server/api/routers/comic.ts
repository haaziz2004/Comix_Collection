import { z } from "zod";

import {
  createTRPCRouter,
  protectedProcedure,
  publicProcedure,
} from "@/server/api/trpc";
import { TRPCError } from "@trpc/server";
import { comicSchema } from "@/lib/validations/comic";

export const comicRouter = createTRPCRouter({
  getAll: publicProcedure.query(async () => {
    const res = await fetch("http://localhost:8080/comics/all");

    if (!res.ok) {
      throw new TRPCError({
        code: "INTERNAL_SERVER_ERROR",
        message: "Failed to fetch comics",
      });
    }

    const data = await res.json();
    const comics = comicSchema.array().parse(data);

    return comics;
  }),
  byId: publicProcedure
    .input(
      z.object({
        id: z.number().int().positive(),
      }),
    )
    .query(async ({ input }) => {
      const res = await fetch(`http://localhost:8080/comics/byId/${input.id}`);

      if (!res.ok) {
        throw new TRPCError({
          code: "INTERNAL_SERVER_ERROR",
          message: "Failed to fetch comics",
        });
      }

      const data = await res.json();
      const comics = comicSchema.parse(data);

      return comics;
    }),
  // addToPersonalCollection: protectedProcedure
  //   .input(
  //     z.object({
  //       id: z.number().int().positive(),
  //     }),
  //   )
  //   .mutation(async ({ ctx, input }) => {

  //   }),
});
