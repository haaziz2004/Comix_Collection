"use client";

import { trpc } from "@/trpc/client";

export default function ComicPage({ params }: { params: { id: string } }) {
  const { data, isLoading, isError } = trpc.comic.byId.useQuery(
    {
      id: parseInt(params.id),
    },
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
    return <div>Error loading comic</div>;
  }

  return (
    <div className="mt-10 flex flex-col items-center justify-center gap-4">
      <div className="flex items-center justify-center gap-2">
        {"Series: "}
        <h1 className="text-xl font-bold">{data?.seriesTitle}</h1>
      </div>
      {data?.storyTitle ? (
        <div className="flex items-center justify-center gap-2">
          {"Story Title: "}
          <h1 className="text-base font-medium">{data?.storyTitle}</h1>
        </div>
      ) : (
        <></>
      )}
      <div className="flex items-center justify-center gap-2">
        {"Volume: "}
        <h1 className="text-base font-medium">{data?.volumeNumber}</h1>
      </div>
      <div className="flex items-center justify-center gap-2">
        {"Issue: "}
        <h1 className="text-base font-medium">{data?.issueNumber}</h1>
      </div>
      {data.description ? (
        <div className="flex items-center justify-center gap-2">
          {"Description: "}
          <h1 className="text-base font-medium">{data?.description}</h1>
        </div>
      ) : (
        <></>
      )}
      <div className="flex items-center justify-center gap-2">
        {"Publisher: "}
        <h1 className="text-base font-medium">{data?.publisher}</h1>
      </div>
    </div>
  );
}
