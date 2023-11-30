import { comicRouter } from "@/server/api/routers/comic";
import { createTRPCRouter } from "@/server/api/trpc";
import { userComicRouter } from "./routers/userComic";
import { authRouter } from "./routers/auth";
import { userRouter } from "./routers/user";

/**
 * This is the primary router for your server.
 *
 * All routers added in /api/routers should be manually added here.
 */
export const appRouter = createTRPCRouter({
  comic: comicRouter,
  userComic: userComicRouter,
  auth: authRouter,
  user: userRouter,
});

// export type definition of API
export type AppRouter = typeof appRouter;
