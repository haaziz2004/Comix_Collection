import { comicSchema } from "@/lib/validations/comic";
import { type NextRequest } from "next/server";

export async function POST(request: NextRequest) {
  const userId = request.cookies.get("userId");

  if (!userId) {
    return new Response("Unauthorized", { status: 401 });
  }

  const res = await request.json();

  if (!res) {
    return new Response("Missing body", { status: 400 });
  }

  const response = await fetch("http://localhost:8080/search/" + userId.value, {
    method: "POST",
    body: JSON.stringify(res),
    cache: "no-store",
  });

  // const comics = comicSchema.array().parse(await response.json());
  const comics = await response.json();

  return new Response(JSON.stringify(comics), {
    status: 200,
    headers: {
      "Content-Type": "application/json",
    },
  });
}
