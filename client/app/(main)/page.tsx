"use client";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "@/components/ui/card";
import { trpc } from "@/trpc/client";
import Link from "next/link";

export default function Home() {
  const { data, isLoading, isError } = trpc.comic.getInfinite.useInfiniteQuery(
    {
      limit: 10,
    },
    {
      getNextPageParam: (lastPage) => {
        return lastPage.nextCursor;
      },
      refetchOnMount: false,
      refetchOnWindowFocus: false,
      refetchOnReconnect: false,
    },
  );

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError) {
    return <div>Error loading comics</div>;
  }

  const comics = data?.pages.map((page) => page.comics).flat();

  return (
    <div className="mt-10 flex h-full w-full items-center justify-center">
      <div className="flex flex-col items-center justify-center">
        <h1 className="mb-10 text-4xl font-bold">Comics</h1>
        <div className="grid grid-cols-3 gap-4">
          {comics?.map((comic) => (
            <Link href={`/comic/${comic.id}`} key={comic.id}>
              <Card>
                <CardHeader className="flex items-center justify-center font-bold">
                  {comic.seriesTitle}
                </CardHeader>
                <CardContent className="text-center">
                  <div className="line-clamp-3">
                    {comic.storyTitle ? comic.storyTitle : "No Title"}
                  </div>
                </CardContent>
                <CardFooter className="flex items-center justify-center">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center">
                      <div className="mr-2">{"Vol. "}</div>
                      <div className="mr-2">{comic.volumeNumber + ","}</div>
                    </div>
                    <div className="flex items-center">
                      <div className="mr-2">{"Issue "}</div>
                      <div className="mr-2">{comic.issueNumber}</div>
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
