import { z } from "zod";

import { createTRPCRouter, protectedProcedure } from "@/server/api/trpc";
import { db } from "@/server/db";
import {
  comics,
  userComicCreators,
  userComics,
  userComicsToUserComicCreators,
  users,
} from "@/server/db/schema";
import { TRPCError } from "@trpc/server";
import { and, eq } from "drizzle-orm";
import { collectionSchema } from "@/lib/validations/collection";

export const userRouter = createTRPCRouter({
  getById: protectedProcedure.query(async ({ ctx }) => {
    return ctx.db.query.users.findFirst({
      columns: {
        username: true,
      },
      where: eq(users.id, ctx.user.id),
    });
  }),
  addComicToPersonalCollection: protectedProcedure
    .input(z.object({ comicId: z.number() }))
    .mutation(async ({ ctx, input }) => {
      const comic = await ctx.db.query.comics.findFirst({
        where: eq(comics.id, input.comicId),
        with: {
          comicsToComicCreators: {
            with: {
              comicCreator: {
                columns: {
                  name: true,
                },
              },
            },
          },
        },
      });

      if (!comic) {
        throw new TRPCError({
          code: "NOT_FOUND",
          message: "Comic not found",
        });
      }

      const user = await ctx.db.query.users.findFirst({
        where: eq(users.id, ctx.user.id),
        with: {
          userComics: {
            where: and(
              eq(userComics.userId, ctx.user.id),
              eq(userComics.publisher, comic.publisher),
              eq(userComics.issueNumber, comic.issueNumber),
              eq(userComics.publicationDate, comic.publicationDate),
              eq(userComics.seriesTitle, comic.seriesTitle),
              eq(userComics.volumeNumber, comic.volumeNumber),
            ),
          },
        },
      });

      if (!user) {
        throw new TRPCError({
          code: "UNAUTHORIZED",
          message: "User not found",
        });
      }

      if (user.userComics.length > 0) {
        throw new TRPCError({
          code: "CONFLICT",
          message: "Comic already in personal collection",
        });
      }

      await db.transaction(async (tx) => {
        const userComic = await tx.insert(userComics).values({
          issueNumber: comic.issueNumber,
          publicationDate: comic.publicationDate,
          seriesTitle: comic.seriesTitle,
          volumeNumber: comic.volumeNumber,
          publisher: comic.publisher,
          storyTitle: comic.storyTitle,
          description: comic.description,
          userId: ctx.user.id,
        });

        const userComicCreatorIds = new Array<number>();

        for (const creator of comic.comicsToComicCreators) {
          const userComicCreator = await tx.insert(userComicCreators).values({
            name: creator.comicCreator.name,
          });
          userComicCreatorIds.push(parseInt(userComicCreator.insertId));
        }
        const insertData = userComicCreatorIds.map((id) => ({
          userComicId: parseInt(userComic.insertId),
          userComicCreatorId: id,
        }));
        await tx.insert(userComicsToUserComicCreators).values(insertData);
      });
    }),
  removeComicFromPersonalCollection: protectedProcedure
    .input(
      z.object({
        comicId: z.number(),
      }),
    )
    .mutation(async ({ ctx, input }) => {
      const comic = await ctx.db.query.userComics.findFirst({
        where: and(
          eq(userComics.id, input.comicId),
          eq(userComics.userId, ctx.user.id),
        ),
      });

      if (!comic) {
        throw new TRPCError({
          code: "NOT_FOUND",
          message: "Comic not found",
        });
      }

      await ctx.db.delete(userComics).where(eq(userComics.id, input.comicId));
    }),
  getPersonalCollection: protectedProcedure.query(async ({ ctx }) => {
    // return ctx.db.query.userComics.findMany({
    //     where: eq(userComics.userId, ctx.user.id),
    //     with: {
    //       userComicsToUserComicCreators: {
    //         with: {
    //           userComicCreator: {
    //             columns: {
    //               name: true,
    //             },
    //           },
    //         },
    //       },
    //       userComicsToUserComicCharacters: {
    //         with: {
    //           userComicCharacter: {
    //             columns: {
    //               name: true,
    //             },
    //           },
    //         },
    //       },
    //     },
    //   });
    const response = await fetch(
      "http://localhost:8080/api/users/" + ctx.user.id + "/collection",
      {
        method: "GET",
        cache: "no-store",
      },
    );

    if (!response.ok) {
      switch (response.status) {
        case 404:
          throw new TRPCError({
            code: "NOT_FOUND",
            message: "Not Found",
          });
        default:
          throw new TRPCError({
            code: "INTERNAL_SERVER_ERROR",
            message: "Internal Server Error",
          });
      }
    }

    if (response.status === 204) {
      throw new TRPCError({
        code: "NOT_FOUND",
        message: "No Content",
      });
    }

    const comics = collectionSchema.parse(await response.json());

    return comics;
  }),
});
