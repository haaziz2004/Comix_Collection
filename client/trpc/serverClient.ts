import { appRouter } from "@/server/api/root";
import { db } from "@/server/db";
import { getCurrentSession } from "@/lib/session";

export async function getServerClient() {
  const session = await getCurrentSession();

  const serverClient = appRouter.createCaller({
    db: db,
    session: session,
  });

  return serverClient;
}

export const serverClient = appRouter.createCaller({
  db: db,
  session: null,
});
