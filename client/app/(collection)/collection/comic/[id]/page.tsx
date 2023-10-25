"use client";

export const dynamic = "force-dynamic";
import { Icons } from "@/components/icons";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { toast } from "@/components/ui/use-toast";
import { comicEditSchema, comicSchema } from "@/lib/validations/comic";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";

const schema = z.object({
  comicId: z.number().int().positive(),
});

type FormData = z.infer<typeof comicEditSchema>;

type ComicId = z.infer<typeof schema>;

type Comic = z.infer<typeof comicSchema>;

interface CollectionCommicPageProps {
  params: { id: string };
}

export default function CollectionComicpage({
  params,
}: CollectionCommicPageProps) {
  const [saveLoading, setSaveLoading] = useState<boolean>(false);
  const [editing, setEditing] = useState<boolean>(false);
  const [submittedData, setSubmittedData] = useState<FormData>();
  const router = useRouter();
  const {
    data: comic,
    isLoading,
    isError,
    refetch,
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

  const {
    register,
    handleSubmit,
    reset,
    formState,
    formState: { errors, isSubmitSuccessful },
  } = useForm<FormData>({
    resolver: zodResolver(comicEditSchema),
    defaultValues: async () => {
      const data = await fetch("http://localhost:8080/comics/" + params.id, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        cache: "no-store",
      });

      if (!data.ok) {
        throw new Error("Error fetching comics");
      }

      const comic = comicSchema.parse(await data.json());

      return {
        publisher: comic.publisher,
        seriesTitle: comic.seriesTitle,
        storyTitle: comic.storyTitle,
        volumeNumber: comic.volumeNumber,
        issueNumber: comic.issueNumber,
        description: comic.description,
        creators: comic.creators
          ? comic.creators.map((item) => item.name.trim()).join(", ")
          : "",
        principleCharacters: comic.principleCharacters
          ? comic.principleCharacters.map((item) => item.name.trim()).join(", ")
          : "",
        publicationDate: comic.publicationDate,
        value: comic.value?.toString() ?? "",
        grade: comic.grade?.toString() ?? "",
        slabbed: comic.slabbed?.toString() ?? "",
      };
    },
  });

  useEffect(() => {
    if (formState.isSubmitSuccessful) {
      reset(submittedData);
    }
  }, [formState, submittedData, reset]);

  const removeFromCollcetionMutation = useMutation({
    mutationKey: ["removeFromCollection"],
    mutationFn: async (input: ComicId) => {
      const data = await fetch("/api/user/removeComic", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(input),
        cache: "no-store",
      });

      if (!data.ok) {
        switch (data.status) {
          case 409:
            throw new Error("CONFLICT");
          case 404:
            throw new Error("NOT FOUND");
          default:
            throw new Error("Error adding comic to collection");
        }
      }

      const comics = comicSchema.array().parse(await data.json());

      return comics;
    },
  });

  const updateComicMutation = useMutation({
    mutationKey: ["updateComic"],
    mutationFn: async (input: FormData) => {
      const data = await fetch(
        "http://localhost:8080/comics/" + params.id + "/update",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(input),
          cache: "no-store",
        },
      );

      if (!data.ok) {
        switch (data.status) {
          case 409:
            throw new Error("CONFLICT");
          case 404:
            throw new Error("NOT FOUND");
          default:
            throw new Error("Error adding comic to collection");
        }
      }

      const comics = comicSchema.parse(await data.json());

      return comics;
    },
  });

  function onSubmit(data: FormData) {
    setSaveLoading(true);
    setSubmittedData(data);

    if (
      !data.publisher &&
      !data.seriesTitle &&
      !data.storyTitle &&
      !data.volumeNumber &&
      !data.issueNumber &&
      !data.description &&
      !data.creators &&
      !data.principleCharacters &&
      !data.publicationDate &&
      !data.value &&
      !data.grade &&
      !data.slabbed
    ) {
      setSaveLoading(false);
      return toast({
        title: "No changes made",
        description: "Please make changes before saving",
        variant: "destructive",
      });
    }

    updateComicMutation.mutate(
      {
        ...data,
      },
      {
        onSuccess: () => {
          setEditing(false);
          refetch();
          return toast({
            title: "Comic updated",
            description: "Comic successfully updated",
            variant: "success",
          });
        },
        onError: (error) => {
          if (error.message === "NOT FOUND") {
            return toast({
              title: "Comic not in personal collection",
              description:
                "You can only update a comic in your collection if it is in your collection",
              variant: "destructive",
            });
          }
          return toast({
            title: "Error updating comic in personal collection",
            description: "Please try again later",
            variant: "destructive",
          });
        },
        onSettled: () => {
          setSaveLoading(false);
        },
      },
    );
  }

  function handleClick() {
    removeFromCollcetionMutation.mutate(
      {
        comicId: parseInt(params.id),
      },
      {
        onSuccess: () => {
          toast({
            title: "Comic removed from personal collection",
            description: "Comic successfully removed from personal collection",
            variant: "success",
          });
          router.push("/collection");
        },
        onError: (error) => {
          if (error.message === "NOT FOUND") {
            return toast({
              title: "Comic not in personal collection",
              description:
                "You can only remove a comic from your collection if it is in your collection",
              variant: "destructive",
            });
          }
          return toast({
            title: "Error removing coimc from personal collection",
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
    <div className="mt-10 flex flex-col items-center justify-center gap-10">
      <div className="flex flex-col items-center justify-center gap-4">
        {editing ? (
          <form onSubmit={handleSubmit(onSubmit)}>
            <div className="flex flex-col items-center justify-center gap-10">
              <div className="grid grid-cols-2 flex-col items-center justify-center gap-6">
                <div className="flex flex-col gap-2">
                  <Label htmlFor="seriesTitle">Series Title</Label>
                  <Input
                    id="seriesTitle"
                    placeholder={comic.seriesTitle}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("seriesTitle")}
                  />
                  {errors?.seriesTitle && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.seriesTitle.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label>Story Title</Label>
                  <Input
                    id="storyTitle"
                    placeholder={comic.storyTitle ?? ""}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("storyTitle")}
                  />
                  {errors?.storyTitle && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.storyTitle.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label htmlFor="volumeNumber">Volume Number</Label>
                  <Input
                    id="volumeNumber"
                    placeholder={comic.volumeNumber}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("volumeNumber")}
                  />
                  {errors?.volumeNumber && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.volumeNumber.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label htmlFor="issueNumber">Issue Number</Label>
                  <Input
                    id="issueNumber"
                    placeholder={comic.issueNumber}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("issueNumber")}
                  />
                  {errors?.issueNumber && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.issueNumber.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label htmlFor="publisher">Publisher</Label>
                  <Input
                    id="publisher"
                    placeholder={comic.publisher ?? ""}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("publisher")}
                  />
                  {errors?.publisher && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.publisher.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label htmlFor="description">Description</Label>
                  <Input
                    id="description"
                    placeholder={comic.description ?? ""}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("description")}
                  />
                  {errors?.description && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.description.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label htmlFor="creators">Creators</Label>
                  <Input
                    id="creators"
                    placeholder={
                      comic.creators
                        ? comic.creators.length !== 0
                          ? comic.creators
                              .map((item) => item.name.trim())
                              .join(", ")
                          : ""
                        : ""
                    }
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("creators")}
                  />
                  {errors?.creators && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.creators.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label htmlFor="characters">Principle Characters</Label>
                  <Input
                    id="characters"
                    placeholder={
                      comic.principleCharacters
                        ? comic.principleCharacters.length !== 0
                          ? comic.principleCharacters
                              .map((item) => item.name.trim())
                              .join(", ")
                          : ""
                        : ""
                    }
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("principleCharacters")}
                  />
                </div>
                {errors?.principleCharacters && (
                  <p className="px-1 text-xs text-red-600">
                    {errors.principleCharacters.message}
                  </p>
                )}
                <div className="flex flex-col gap-2">
                  <Label htmlFor="publicationDate">Publication Date</Label>
                  <Input
                    id="publicationDate"
                    placeholder={comic.publicationDate}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("publicationDate")}
                  />
                  {errors?.publicationDate && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.publicationDate.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label htmlFor="value">Value</Label>
                  <Input
                    id="value"
                    placeholder={comic.value?.toString() ?? "value"}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("value")}
                  />
                  {errors?.value && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.value.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label htmlFor="grade">Grade</Label>
                  <Input
                    id="grade"
                    placeholder={comic.grade?.toString() ?? "grade"}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("grade")}
                  />
                  {errors?.grade && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.grade.message}
                    </p>
                  )}
                </div>
                <div className="flex flex-col gap-2">
                  <Label htmlFor="slabbed">Slabbed</Label>
                  <Input
                    id="slabbed"
                    placeholder={comic.slabbed?.toString() ?? "slabbed"}
                    type="text"
                    autoCapitalize="none"
                    disabled={
                      removeFromCollcetionMutation.isPending || saveLoading
                    }
                    {...register("slabbed")}
                  />
                  {errors?.slabbed && (
                    <p className="px-1 text-xs text-red-600">
                      {errors.slabbed.message}
                    </p>
                  )}
                </div>
              </div>
              <div className="flex items-center justify-center gap-4">
                <Button
                  onClick={() => {
                    setEditing((editing) => !editing);
                  }}
                  disabled={
                    removeFromCollcetionMutation.isPending || saveLoading
                  }
                >
                  Cancel
                </Button>

                <Button
                  disabled={
                    removeFromCollcetionMutation.isPending || saveLoading
                  }
                  type="submit"
                >
                  {(removeFromCollcetionMutation.isPending || saveLoading) && (
                    <Icons.spinner className="mr-2 h-4 w-4 animate-spin" />
                  )}
                  Save
                </Button>
              </div>
            </div>
          </form>
        ) : (
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
              {comic.creators && comic.creators.length > 0 && (
                <>
                  <div>Creators</div>
                  <div>
                    {comic.creators.map((item) => item.name.trim()).join(", ")}
                  </div>
                </>
              )}
              {comic.principleCharacters &&
                comic.principleCharacters.length > 0 && (
                  <>
                    <div>Principle Characters</div>
                    <div>
                      {comic.principleCharacters
                        .map((item) => item.name.trim())
                        .join(", ")}
                    </div>
                  </>
                )}
              <div>Publication Date</div>
              <div>{comic.publicationDate}</div>
              {comic.value !== 0 && (
                <>
                  <div>Value</div>
                  <div>{comic.value?.toString()}</div>
                </>
              )}
              {comic.grade !== 0 && (
                <>
                  <div>Grade</div>
                  <div>{comic.grade?.toString()}</div>
                </>
              )}
              <div>Slabbed</div>
              <div>{comic.slabbed?.toString() ?? "slabbed"}</div>
            </div>
          </div>
        )}
      </div>
      {editing ? (
        <></>
      ) : (
        <Button onClick={() => setEditing((editing) => !editing)}>Edit</Button>
      )}
      <Button
        onClick={handleClick}
        disabled={removeFromCollcetionMutation.isPending || saveLoading}
      >
        {removeFromCollcetionMutation.isPending && (
          <Icons.spinner className="mr-2 h-4 w-4 animate-spin" />
        )}
        Remove from Personal Collection
      </Button>
    </div>
  );
}
