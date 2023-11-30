"use client";

export const dynamic = "force-dynamic";

import { Icons } from "@/components/icons";
import { Button } from "@/components/ui/button";
import { toast } from "@/components/ui/use-toast";
import { api } from "@/trpc/react";

export default function ComicPage({ params }: { params: { id: string } }) {
  const {
    data: comic,
    isLoading,
    isError,
  } = api.comic.getById.useQuery(
    { id: parseInt(params.id) },
    {
      refetchOnMount: false,
      refetchOnReconnect: false,
      refetchOnWindowFocus: false,
    },
  );

  const utils = api.useUtils();

  const addComicToPersonalCollectionMutation =
    api.user.addComicToPersonalCollection.useMutation();

  function handleClick() {
    addComicToPersonalCollectionMutation.mutate(
      { comicId: parseInt(params.id) },
      {
        onSuccess() {
          void utils.user.getPersonalCollection.invalidate();
          toast({
            title: "Comic added to personal collection",
            variant: "success",
          });
        },
        onError(error) {
          toast({
            title: "Error adding comic to personal collection",
            description: error.message,
            variant: "destructive",
          });
        },
      },
    );
  }

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError) {
    return <div>Error loading comic</div>;
  }

  if (!comic) {
    return <div>Comic not found</div>;
  }

  return (
    <div className="mt-10 flex flex-col items-center justify-center gap-4">
      <div className="flex flex-col items-center justify-center gap-10">
        <div className="grid grid-cols-2 flex-col items-center justify-center gap-4">
          <div>Series Title</div>
          <div>{comic.seriesTitle}</div>
          {comic.storyTitle && (
            <>
              <div>Story Title</div>
              <div>{comic.storyTitle}</div>
            </>
          )}
          <div>Volume Number</div>
          <div>{comic.volumeNumber}</div>
          <div>Issue Number</div>
          <div>{comic.issueNumber}</div>
          {comic.description && (
            <>
              <div>Description</div>
              <div>{comic.description}</div>
            </>
          )}
          {comic.creators && comic.creators?.length > 0 && (
            <>
              <div>Creators</div>
              <div>
                {comic.creators.map((creator) => creator.name).join(", ")}
              </div>
            </>
          )}
          <div>Publication Date</div>
          <div>
            {comic.publicationDate.toLocaleString("en-US", {
              year: "numeric",
              month: "long",
              day: "numeric",
            })}
          </div>
        </div>
      </div>
      <Button
        onClick={handleClick}
        disabled={addComicToPersonalCollectionMutation.isLoading}
      >
        {addComicToPersonalCollectionMutation.isLoading && (
          <Icons.spinner className="mr-2 h-4 w-4 animate-spin" />
        )}
        Add to Personal Collection
      </Button>
    </div>
  );
}
