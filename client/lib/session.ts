"use server";
import { db } from "@/server/db";
import { users } from "@/server/db/schema";
import { eq } from "drizzle-orm";
import { cookies } from "next/headers";

export async function getCurrentUser() {
  const userId = cookies().get("user-id");

  if (!userId?.value) {
    return null;
  }

  const user = await db.query.users.findFirst({
    where: eq(users.id, parseInt(userId.value)),
  });

  if (!user) {
    return null;
  }

  return user;
}

// eslint-disable-next-line @typescript-eslint/require-await
export async function signOut() {
  // clear cookies
  cookies().set("user-id", "", {
    maxAge: -1,
  });
}
