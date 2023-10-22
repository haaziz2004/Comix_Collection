import { comicRouter } from "./routers/comic";
// import { userRouter } from "./routers/old.user";
import { createTRPCRouter } from "./trpc";

export const appRouter = createTRPCRouter({
  // user: userRouter,
  comic: comicRouter,
});

export type AppRouter = typeof appRouter;
