import { comicSchema } from "@/lib/validations/comic";
import { type NextRequest } from "next/server";

export async function GET(request: NextRequest) {
  const userId = request.cookies.get("userId");

  if (!userId) {
    return new Response("Unauthorized", { status: 401 });
  }

  const response = await fetch(
    "http://localhost:8080/users/" + userId.value + "/collection",
    {
      method: "GET",
      cache: "no-store",
    },
  );

  // const comics = comicSchema.array().parse(await response.json());
  const comics = await response.json();

  return new Response(JSON.stringify(comics), {
    status: 200,
    headers: {
      "Content-Type": "application/json",
    },
  });
}
