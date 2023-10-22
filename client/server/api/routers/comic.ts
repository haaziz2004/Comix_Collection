import { z } from "zod";

import {
  createTRPCRouter,
  protectedProcedure,
  publicProcedure,
} from "@/server/api/trpc";
import { TRPCError } from "@trpc/server";

export const comicRouter = createTRPCRouter({
  getInfinite: publicProcedure
    .input(
      z.object({
        limit: z.number().min(1).max(50).nullish(),
        cursor: z.number().nullish(),
      }),
    )
    .query(async ({ ctx, input }) => {
      const limit = input.limit ?? 10;
      const cursor = input.cursor;

      const comics = await ctx.db.comic.findMany({
        take: limit + 1,
        cursor: cursor ? { id: cursor } : undefined,
        orderBy: [
          { seriesTitle: "asc" },
          { volumeNumber: "asc" },
          { issueNumber: "asc" },
        ],
      });

      if (!comics) {
        throw new TRPCError({
          code: "NOT_FOUND",
          message: "No comics found",
        });
      }

      let nextCursor: typeof cursor | undefined = undefined;
      if (comics.length > limit) {
        const nextItem = comics.pop();
        nextCursor = nextItem!.id;
      }

      return { comics, nextCursor };
    }),
  byId: publicProcedure
    .input(
      z.object({
        id: z.number().int().positive(),
      }),
    )
    .query(async ({ ctx, input }) => {
      const comic = await ctx.db.comic.findUnique({
        where: { id: input.id },
      });

      if (!comic) {
        throw new TRPCError({
          code: "NOT_FOUND",
          message: "Comic not found",
        });
      }

      return comic;
    }),
  addToPersonalCollection: protectedProcedure
    .input(
      z.object({
        id: z.number().int().positive(),
      }),
    )
    .mutation(async ({ ctx, input }) => {
      const user = ctx.session.user;

      // find comic
      const comic = await ctx.db.comic.findUnique({
        where: { id: input.id },
      });

      if (!comic) {
        throw new TRPCError({
          code: "NOT_FOUND",
          message: "Comic not found",
        });
      }

      // check if it's already in the user's collection
      const personalCollection = await ctx.db.personalCollection.findUnique({
        where: { userId: user.id },
        include: {
          userComics: {
            where: { comicId: input.id },
          },
        },
      });

      if (personalCollection?.userComics.length !== 0) {
        throw new TRPCError({
          code: "CONFLICT",
          message: "Comic already exists in personal collection",
        });
      }

      // make it a userComic
      const userComic = await ctx.db.userComic.upsert({
        where: {
          userId_comicId: {
            userId: user.id,
            comicId: input.id,
          },
        },
        create: {
          userId: user.id,
          comicId: input.id,
        },
        update: {},
      });

      // add it to the user's personal collection and create a user's personal collection if it doesn't exist
      const updatedPersonalCollection = await ctx.db.personalCollection.upsert({
        where: {
          userId: user.id,
        },
        create: {
          userId: user.id,
          userComics: {
            connect: {
              id: userComic.id,
            },
          },
        },
        update: {
          userComics: {
            connect: {
              id: userComic.id,
            },
          },
        },
      });

      return updatedPersonalCollection;
    }),
});
