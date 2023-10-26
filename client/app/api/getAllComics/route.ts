import { db } from "@/lib/db";

export async function GET() {
  const comics = await db.comic.findMany();
  console.log(comics);

  if (!comics) {
    throw new Error("No comics found");
  }
  try {
    return new Response(
      JSON.stringify(
        JSON.parse(
          JSON.stringify(
            this,
            (key, value) =>
              typeof value === "bigint" ? value.toString() : value, // return everything else unchanged
          ),
        ),
      ),
    );
  } catch (error) {
    console.error(error);
    return new Response("Error: " + error.message);
  }
}
