"use server";
import { cookies } from "next/headers";
import { userSchema } from "./validations/user";

export async function getCurrentUser() {
  const userId = cookies().get("userId");

  if (!userId) {
    return null;
  }

  const userResult = await fetch(
    `http://localhost:8080/users/${userId.value}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    },
  );

  if (!userResult.ok) {
    return null;
  }

  const user = userSchema.parse(await userResult.json());

  return user;
}

// eslint-disable-next-line @typescript-eslint/require-await
export async function signOut() {
  // clear cookies
  cookies().set("userId", "", {
    maxAge: -1,
  });
}
