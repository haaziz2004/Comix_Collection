"use client";

import { Button } from "@/components/ui/button";
import { toast } from "@/components/ui/use-toast";
import { comicSchema } from "@/lib/validations/comic";
import { useQuery } from "@tanstack/react-query";

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

  // const addToCollectionMutation =
  //   trpc.comic.addToPersonalCollection.useMutation();

  // const utils = trpc.useContext();

  // function handleClick() {
  //   addToCollectionMutation.mutate(
  //     {
  //       id: parseInt(params.id),
  //     },
  //     {
  //       onSuccess: () => {
  //         // TODO: doesn't work
  //         void utils.user.getPersonalCollection.invalidate();
  //         return toast({
  //           title: "Comic added to personal collection",
  //           variant: "success",
  //         });
  //       },
  //       onError: (error) => {
  //         if (error.data?.code === "CONFLICT") {
  //           return toast({
  //             title: "Comic already in personal collection",
  //             description: "You can only add a comic to your collection once",
  //             variant: "destructive",
  //           });
  //         }
  //         return toast({
  //           title: "Error adding comic to personal collection",
  //           description: "Please try again later",
  //           variant: "destructive",
  //         });
  //       },
  //     },
  //   );
  // }

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError) {
    return <div>Error loading comic</div>;
  }

  return (
    <div className="mt-10 flex flex-col items-center justify-center gap-4">
      <div className="flex flex-col items-center justify-center gap-4">
        <div className="flex items-center justify-center gap-2">
          {"Series: "}
          <h1 className="text-xl font-bold">{comic?.seriesTitle}</h1>
        </div>
        {comic?.storyTitle ? (
          <div className="flex items-center justify-center gap-2">
            {"Story Title: "}
            <h1 className="text-base font-medium">{comic?.storyTitle}</h1>
          </div>
        ) : (
          <></>
        )}
        <div className="flex items-center justify-center gap-2">
          {"Volume: "}
          <h1 className="text-base font-medium">{comic?.volumeNumber}</h1>
        </div>
        <div className="flex items-center justify-center gap-2">
          {"Issue: "}
          <h1 className="text-base font-medium">{comic?.issueNumber}</h1>
        </div>
        {comic?.description ? (
          <div className="flex items-center justify-center gap-2">
            {"Description: "}
            <h1 className="text-base font-medium">{comic?.description}</h1>
          </div>
        ) : (
          <></>
        )}
        <div className="flex items-center justify-center gap-2">
          {"Publisher: "}
          <h1 className="text-base font-medium">{comic?.publisher}</h1>
        </div>
      </div>
      {/* <Button onClick={handleClick}>Add to Personal Collection</Button> */}
    </div>
  );
}
