"use client";

export const dynamic = "force-dynamic";
import { Icons } from "@/components/icons";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { toast } from "@/components/ui/use-toast";
import { comicEditSchema, comicSchema } from "@/lib/validations/comic";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { type z } from "zod";

type FormData = z.infer<typeof comicEditSchema>;

export default function CreatePage() {
  const [loading, setLoading] = useState(false);
  const {
    register,
    handleSubmit,
    reset,
    formState,
    formState: { errors, isSubmitSuccessful },
  } = useForm<FormData>({
    resolver: zodResolver(comicEditSchema),
    defaultValues: {
      // creators: "",
      // description: "",
      // grade: "",
      // issueNumber: "",
      // publicationDate: "",
      // seriesTitle: "",
      // slabbed: "",
      // storyTitle: "",
      // value: "",
      // volumeNumber: "",
      // principleCharacters: "",
      // publisher: "",
    },
  });

  useEffect(() => {
    if (isSubmitSuccessful) {
      reset();
    }
  }, [formState, reset]);

  const createComicMutation = useMutation({
    mutationKey: ["createComic"],
    mutationFn: async (input: FormData) => {
      console.log("input", input);
      const data = await fetch("api/user/createComic", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(input),
        cache: "no-store",
      });

      console.log(data.ok, data.status, data.statusText, data.body);

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

      return;
    },
  });

  function onSubmit(data: FormData) {
    console.log(data);
    setLoading(true);

    createComicMutation.mutate(
      {
        ...data,
      },
      {
        onSuccess: () => {
          setLoading(false);
          return toast({
            title: "Comic created",
            description:
              "Comic successfully created and added to personal collection.",
            variant: "success",
          });
        },
        onError: (error) => {
          return toast({
            title: "Error creating comic.",
            description: "Please try again later",
            variant: "destructive",
          });
        },
        onSettled: () => {
          setLoading(false);
        },
      },
    );
  }

  return (
    <div className="mt-10 flex flex-col items-center justify-center gap-10">
      <h1 className="text-4xl font-bold">Create Comic</h1>

      <div className="flex flex-col items-center justify-center gap-4">
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="flex flex-col items-center justify-center gap-10">
            <div className="grid grid-cols-2 flex-col items-center justify-center gap-6">
              <div className="flex flex-col gap-2">
                <Label htmlFor="seriesTitle">Series Title</Label>
                <Input
                  id="seriesTitle"
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                  placeholder={""}
                  type="text"
                  autoCapitalize="none"
                  disabled={createComicMutation.isPending || loading}
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
                disabled={createComicMutation.isPending || loading}
                type="submit"
              >
                {(createComicMutation.isPending || loading) && (
                  <Icons.spinner className="mr-2 h-4 w-4 animate-spin" />
                )}
                Create and add to collection
              </Button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}
