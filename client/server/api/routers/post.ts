import { createTRPCRouter, publicProcedure } from "@/server/api/trpc";
import { TRPCError } from "@trpc/server";

export const comicsRouter = createTRPCRouter({
  getAll: publicProcedure.query(async ({ ctx }) => {
    const comics = await ctx.db.comic.findMany();

    console.log(comics);

    if (!comics) {
      throw new TRPCError({
        code: "NOT_FOUND",
      });
    }

    return comics;
  }),
});
