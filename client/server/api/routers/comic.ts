import { z } from "zod";

import { createTRPCRouter, publicProcedure } from "@/server/api/trpc";
import { comics } from "@/server/db/schema";
import { asc, eq, like } from "drizzle-orm";
import { TRPCError } from "@trpc/server";
import { getAllSchema, getByIdSchema } from "@/lib/schemas";

export const comicRouter = createTRPCRouter({
  getAll: publicProcedure
    .input(
      z
        .object({
          page: z.number(),
          perPage: z.number(),
        })
        .optional(),
    )
    .query(async ({ ctx, input }) => {
      // return ctx.db
      //   .select()
      //   .from(comics)
      //   .limit(input?.perPage ?? 100)
      //   .offset(input ? input.page * input.perPage : 0)
      //   .orderBy(
      //     asc(comics.seriesTitle),
      //     asc(comics.volumeNumber),
      //     asc(comics.issueNumber),
      //     asc(comics.publicationDate),
      //   );
      const response = await fetch("http://localhost:8080/api/comics/getAll");

      if (!response.ok) {
        switch (response.status) {
          case 404:
            throw new TRPCError({
              code: "NOT_FOUND",
              message: "No comics found",
            });
          default:
            throw new TRPCError({
              code: "INTERNAL_SERVER_ERROR",
              message: "Internal server error",
            });
        }
      }

      const comics = getAllSchema.parse(await response.json());

      return comics;
    }),

  getById: publicProcedure
    .input(z.object({ id: z.number() }))
    .query(async ({ ctx, input }) => {
      // return ctx.db.query.comics.findFirst({
      //   where: eq(comics.id, input.id),
      //   with: {
      //     comicsToComicCreators: {
      //       with: {
      //         comicCreator: {
      //           columns: {
      //             name: true,
      //           },
      //         },
      //       },
      //     },
      //   },
      // });
      const response = await fetch(
        "http://localhost:8080/api/comics/getById/" + input.id,
      );

      if (!response.ok) {
        switch (response.status) {
          case 404:
            throw new TRPCError({
              code: "NOT_FOUND",
              message: "No comics found",
            });
          default:
            throw new TRPCError({
              code: "INTERNAL_SERVER_ERROR",
              message: "Internal server error",
            });
        }
      }

      const comics = getByIdSchema.parse(await response.json());

      return comics;
    }),

  search: publicProcedure
    .input(
      z.object({
        queryString: z.string(),
        sortType: z.enum(["ALPHABETICAL", "DATE"]),
        searchType: z.enum(["PARTIAL", "EXACT"]),
      }),
    )
    .mutation(async ({ ctx, input }) => {
      // return ctx.db
      //   .select()
      //   .from(comics)
      //   .where(
      //     input.searchType === "PARTIAL"
      //       ? like(comics.seriesTitle, `%${input.queryString}%`)
      //       : eq(comics.seriesTitle, input.queryString),
      //   )
      //   .orderBy(
      //     ...(input.sortType === "ALPHABETICAL"
      //       ? [
      //           asc(comics.seriesTitle),
      //           asc(comics.volumeNumber),
      //           asc(comics.issueNumber),
      //           asc(comics.publicationDate),
      //         ]
      //       : [asc(comics.publicationDate)]),
      //   );
      const response = await fetch("http://localhost:8080/api/comics/search", {
        method: "POST",
        body: JSON.stringify(input),
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        switch (response.status) {
          case 404:
            throw new TRPCError({
              code: "NOT_FOUND",
              message: "No comics found",
            });
          default:
            throw new TRPCError({
              code: "INTERNAL_SERVER_ERROR",
              message: "Internal server error",
            });
        }
      }

      const comics = getAllSchema.parse(await response.json());

      return comics;
    }),
});
