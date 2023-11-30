import { comicSchema } from "@/lib/validations/comic";
import { type NextRequest } from "next/server";
import { z } from "zod";

const schema = z.object({
  comicId: z.number().int().positive(),
});

export async function POST(request: NextRequest) {
  const userId = request.cookies.get("userId");

  if (!userId) {
    return new Response("Unauthorized", { status: 401 });
  }

  const res = await request.json();

  if (!res) {
    return new Response("Missing body", { status: 400 });
  }

  const comicId = schema.parse(res).comicId;

  if (!comicId) {
    return new Response("Missing comicId", { status: 400 });
  }

  const response = await fetch(
    "http://localhost:8080/users/" + userId.value + "/comics/remove/" + comicId,
    {
      method: "POST",
      cache: "no-store",
    },
  );

  if (!response.ok) {
    switch (response.status) {
      case 400:
        return new Response("Bad Request", { status: 400 });
      case 404:
        return new Response("Not Found", { status: 404 });
      case 409:
        return new Response("Conflict", { status: 409 });
      case 500:
        return new Response("Internal Server Error", { status: 500 });
      default:
        return new Response("Unknown Error", { status: 500 });
    }
  }

  const comics = comicSchema.array().parse(await response.json());

  return new Response(JSON.stringify(comics), {
    status: 200,
    headers: {
      "Content-Type": "application/json",
    },
  });
}
