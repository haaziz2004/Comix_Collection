"use client";

export const dynamic = "force-dynamic";

import { Icons } from "@/components/icons";
import { Button } from "@/components/ui/button";
import { toast } from "@/components/ui/use-toast";
import { comicSchema } from "@/lib/validations/comic";
import { useMutation, useQuery } from "@tanstack/react-query";
import { z } from "zod";

const schema = z.object({
  comicId: z.number().int().positive(),
});

type ComicId = z.infer<typeof schema>;

export default function ComicPage({ params }: { params: { id: string } }) {
  const {
    data: comic,
    isLoading,
    isError,
  } = useQuery({
    queryKey: ["comic", params.id],
    queryFn: async () => {
      const data = await fetch("http://localhost:8080/comics/" + params.id);

      if (!data.ok) {
        throw new Error("Error fetching comics");
      }

      return comicSchema.parse(await data.json());
    },
  });

  const addToCollectionMutation = useMutation({
    mutationKey: ["addToCollection"],
    mutationFn: async (input: ComicId) => {
      console.log(input);
      const data = await fetch("/api/user/addComic", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(input),
      });

      if (!data.ok) {
        switch (data.status) {
          case 409:
            throw new Error("CONFLICT");
          case 404:
            throw new Error("NOT FOUND");
          case 401:
            throw new Error("UNAUTHORIZED");
          default:
            throw new Error("Error adding comic to collection");
        }
      }

      const comics = comicSchema.array().parse(await data.json());

      return comics;
    },
  });

  function handleClick() {
    addToCollectionMutation.mutate(
      {
        comicId: parseInt(params.id),
      },
      {
        onSuccess: () => {
          return toast({
            title: "Comic added to personal collection",
            variant: "success",
          });
        },
        onError: (error) => {
          if (error.message === "CONFLICT") {
            return toast({
              title: "Comic already in personal collection",
              description: "You can only add a comic to your collection once",
              variant: "destructive",
            });
          }
          if (error.message === "UNAUTHORIZED") {
            return toast({
              title: "Unauthorized",
              description:
                "You must be logged in to add a comic to your collection",
              variant: "destructive",
            });
          }
          return toast({
            title: "Error adding comic to personal collection",
            description: "Please try again later",
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
                {comic.creators
                  .map((item: { name: string }) => item.name.trim())
                  .join(", ")}
              </div>
            </>
          )}
          {comic.principleCharacters &&
            comic.principleCharacters?.length > 0 && (
              <>
                <div>Principle Characters</div>
                <div>
                  {comic.principleCharacters
                    .map((item: { name: string }) => item.name.trim())
                    .join(", ")}
                </div>
              </>
            )}
          <div>Publication Date</div>
          <div>{comic.publicationDate}</div>
        </div>
      </div>
      <Button
        onClick={handleClick}
        disabled={addToCollectionMutation.isPending}
      >
        {addToCollectionMutation.isPending && (
          <Icons.spinner className="mr-2 h-4 w-4 animate-spin" />
        )}
        Add to Personal Collection
      </Button>
    </div>
  );
}
