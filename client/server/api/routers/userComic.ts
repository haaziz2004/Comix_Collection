import { z } from "zod";

import {
  createTRPCRouter,
  protectedProcedure,
  publicProcedure,
} from "@/server/api/trpc";
import { asc, eq, like } from "drizzle-orm";
import { userComics } from "@/server/db/schema";

export const userComicRouter = createTRPCRouter({
  getAll: protectedProcedure.query(({ ctx }) => {
    return ctx.db.query.userComics.findMany({
      where: eq(userComics.userId, ctx.user.id),
      orderBy: [
        asc(userComics.seriesTitle),
        asc(userComics.volumeNumber),
        asc(userComics.issueNumber),
        asc(userComics.publicationDate),
      ],
      with: {
        userComicsToUserComicCharacters: true,
        userComicsToUserComicCreators: true,
      },
    });
  }),

  search: protectedProcedure
    .input(
      z.object({
        queryString: z.string(),
        sortType: z.enum(["ALPHABETICAL", "DATE"]),
        searchType: z.enum(["PARTIAL", "EXACT"]),
      }),
    )
    .mutation(async ({ ctx, input }) => {
      return ctx.db
        .select()
        .from(userComics)
        .where(
          input.searchType === "PARTIAL"
            ? like(userComics.seriesTitle, `%${input.queryString}%`)
            : eq(userComics.seriesTitle, input.queryString),
        )
        .orderBy(
          ...(input.sortType === "ALPHABETICAL"
            ? [
                asc(userComics.seriesTitle),
                asc(userComics.volumeNumber),
                asc(userComics.issueNumber),
                asc(userComics.publicationDate),
              ]
            : [asc(userComics.publicationDate)]),
        );
    }),
});
