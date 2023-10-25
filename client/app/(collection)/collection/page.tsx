"use client";
export const dynamic = "force-dynamic";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardHeader,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import { comicSchema } from "@/lib/validations/comic";
import { useQuery } from "@tanstack/react-query";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function PersonalCollectionPage() {
  const [level, setLevel] = useState(4);
  const router = useRouter();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["userComics"],
    queryFn: async () => {
      const data = await fetch("api/user/getComics");

      if (!data.ok) {
        throw new Error("Error fetching comics");
      }

      // return comicSchema.array().parse(await data.json());
      return await data.json();
    },
  });

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError) {
    return <div>Error loading personal collection</div>;
  }

  if (!data) {
    return <div>No comics in personal collection</div>;
  }

  return (
    <div className="mt-10 flex h-full w-full items-center justify-center">
      <div className="flex flex-col items-center justify-center">
        <h1 className="mb-10 text-4xl font-bold">Personal Collection</h1>
        {data.elements.length === 0 && (
          <div className="flex flex-col items-center justify-center gap-5">
            <div className="text-center">No comics in personal collection</div>
            <Button
              onClick={() => {
                router.push("/");
              }}
            >
              View Comics
            </Button>
          </div>
        )}
        <div className="grid grid-cols-3 gap-4">
          {/* check if level = 4 and then loop over data elements */}
          {level === 4 &&
            data.elements[0].elements[0].elements[0].elements[0].elements.map(
              (comic) => {
                return (
                  <Card
                    key={comic.id}
                    onClick={() => {
                      setLevel(1);
                    }}
                  >
                    <CardHeader>
                      <div className="flex items-center justify-center gap-2">
                        <div className="text-xl font-bold">
                          {comic.seriesTitle}
                        </div>
                      </div>
                    </CardHeader>
                    <CardContent>
                      <div className="flex items-center justify-center gap-2">
                        <div className="text-xl font-bold">
                          {comic.storyTitle}
                        </div>
                      </div>
                    </CardContent>
                    <CardFooter>
                      <div className="flex items-center justify-center gap-2">
                        <div className="text-xl font-bold">{comic.value}</div>
                      </div>
                    </CardFooter>
                  </Card>
                );
              },
            )}

          <div className="flex flex-col items-center justify-center">
            <div>Value: {data.value}</div>
            <div>Number of issues: {data.numberOfIssues}</div>
          </div>
        </div>
      </div>
    </div>
  );
}
