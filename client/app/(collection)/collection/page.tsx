"use client";

import {
  Card,
  CardHeader,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import { trpc } from "@/trpc/client";
import Link from "next/link";

export default function PersonalCollectionPage() {
  const { data, isLoading, isError } = trpc.user.getPersonalCollection.useQuery(
    undefined,
    {
      refetchOnMount: false,
      refetchOnReconnect: false,
      refetchOnWindowFocus: false,
    },
  );

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
        <div className="grid grid-cols-3 gap-4">
          {data.userComics.map((userComic) => (
            <Link href={`/comic/${userComic.id}`} key={userComic.id}>
              <Card>
                <CardHeader className="flex items-center justify-center font-bold">
                  {userComic.comic.seriesTitle}
                </CardHeader>
                <CardContent className="text-center">
                  <div className="line-clamp-3">
                    {userComic.comic.storyTitle
                      ? userComic.comic.storyTitle
                      : "No Title"}
                  </div>
                </CardContent>
                <CardFooter className="flex items-center justify-center">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center">
                      <div className="mr-2">{"Vol. "}</div>
                      <div className="mr-2">
                        {userComic.comic.volumeNumber + ","}
                      </div>
                    </div>
                    <div className="flex items-center">
                      <div className="mr-2">{"Issue "}</div>
                      <div className="mr-2">{userComic.comic.issueNumber}</div>
                    </div>
                  </div>
                </CardFooter>
              </Card>
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}
