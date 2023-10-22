import { z } from "zod";

import {
  createTRPCRouter,
  protectedProcedure,
  publicProcedure,
} from "@/server/api/trpc";

import { userNameSchema } from "@/lib/validations/user";

export const userRouter = createTRPCRouter({
  editUsername: protectedProcedure
    .input(userNameSchema)
    .mutation(async ({ ctx, input }) => {
      const user = ctx.session?.user;
      const username = input.name;

      const updatedUser = await ctx.db.user.update({
        where: {
          id: user.id,
        },
        data: {
          name: username,
        },
      });

      return updatedUser;
    }),
  getPersonalCollection: protectedProcedure.query(async ({ ctx }) => {
    const user = ctx.session?.user;

    const personaCollection = await ctx.db.personalCollection.findFirst({
      where: {
        userId: user.id,
      },
      include: {
        userComics: {
          select: {
            id: true,
            comic: true,
            graded: true,
            slapped: true,
          },
          orderBy: [
            {
              comic: {
                seriesTitle: "desc",
              },
            },
            {
              comic: {
                volumeNumber: "desc",
              },
            },
            {
              comic: {
                issueNumber: "desc",
              },
            },
          ],
        },
      },
    });

    return personaCollection;
  }),
});
