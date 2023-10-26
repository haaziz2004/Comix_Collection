/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-misused-promises */
"use client";
// export const dynamic = "force-dynamic";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardHeader,
  CardContent,
  CardFooter,
  CardTitle,
} from "@/components/ui/card";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
  FormControl,
} from "@/components/ui/form";
import { comicEditSchema, comicSchema } from "@/lib/validations/comic";
import { useMutation, useQuery } from "@tanstack/react-query";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { Input } from "@/components/ui/input";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { toast } from "@/components/ui/use-toast";
import { Icons } from "@/components/icons";
import { Label } from "@/components/ui/label";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";

const formSchema = z.object({
  queryString: z.string().min(1),
  searchType: z.enum(["EXACT", "PARTIAL"]),
  sortType: z.enum(["ALPHABETICAL", "DATE"]),
});

const schema = z.object({
  comicId: z.number().int().positive(),
});

type FormData = z.infer<typeof comicEditSchema>;

type ComicId = z.infer<typeof schema>;

type Comic = z.infer<typeof comicSchema>;

export default function PersonalCollectionPage() {
  const [saveLoading, setSaveLoading] = useState<boolean>(false);
  const [editing, setEditing] = useState<boolean>(false);
  const [submittedData, setSubmittedData] = useState<FormData>();
  const router = useRouter();

  const [currentLevel, setCurrentLevel] = useState<string>("publishers");
  const [selectedPublisher, setSelectedPublisher] = useState<string | null>(
    null,
  );
  const [selectedSeries, setSelectedSeries] = useState<string | null>(null);
  const [selectedVolume, setSelectedVolume] = useState<string | null>(null);
  const [selectedIssueId, setSelectedIssueId] = useState<number | null>(null);
  const [numberOfIssues, setNumberOfIssues] = useState<number>(0);
  const [value, setValue] = useState<number>(0);

  // Function to switch to a different level
  const switchLevel = (level: string) => {
    setCurrentLevel(level);
  };

  // Function to select a publisher
  const selectPublisher = (publisher: string) => {
    setSelectedPublisher(publisher);
    switchLevel("series");
  };

  // Function to select a series
  const selectSeries = (seriesTitle: string) => {
    setSelectedSeries(seriesTitle);
    switchLevel("volumes");
  };

  // Function to select a volume
  const selectVolume = (volume: string) => {
    setSelectedVolume(volume);
    switchLevel("issues");
  };

  const { data, isLoading, isError, refetch } = useQuery({
    queryKey: ["userComics"],
    queryFn: async () => {
      const data = await fetch("api/user/getComics", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        // cache: "no-cache",
      });

      if (!data.ok) {
        throw new Error("Error fetching comics");
      }

      const json = await data.json();

      setNumberOfIssues(json.numberOfIssues);
      setValue(json.value);

      return json;
    },
  });

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      queryString: "",
      searchType: "PARTIAL",
      sortType: "ALPHABETICAL",
    },
    mode: "onSubmit",
    reValidateMode: "onSubmit",
  });

  const searchMutation = useMutation({
    mutationKey: ["search"],
    mutationFn: async (values: z.infer<typeof formSchema>) => {
      const data = await fetch("http://localhost:8080/comics/search", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(values),
      });

      if (!data.ok) {
        if (data.status === 404) {
          return [];
        }
        throw new Error("Error fetching comics");
      }

      return comicSchema.array().parse(await data.json());
    },
  });

  function onSubmit(values: z.infer<typeof formSchema>) {
    searchMutation.mutate(values);
  }

  const {
    register,
    handleSubmit,
    reset,
    formState,
    formState: { errors, isSubmitSuccessful },
  } = useForm<FormData>({
    resolver: zodResolver(comicEditSchema),
  });

  form.formState;

  useEffect(() => {
    if (formState.isSubmitSuccessful) {
      reset(submittedData);
    }
  }, [formState, submittedData, reset]);

  useEffect(() => {
    // Define a function to determine if there is data at the current level
    const hasDataAtCurrentLevel = () => {
      if (currentLevel === "publishers") {
        return data.elements && data.elements.length > 0;
      }
      if (currentLevel === "series") {
        return (
          data.elements &&
          data.elements.find(
            (publisher) => publisher.publisher === selectedPublisher,
          )?.elements.length > 0
        );
      }
      if (currentLevel === "volumes") {
        return (
          data.elements &&
          data.elements
            .find((publisher) => publisher.publisher === selectedPublisher)
            ?.elements.find((series) => series.seriesTitle === selectedSeries)
            ?.elements.length > 0
        );
      }
      if (currentLevel === "issues") {
        return (
          data.elements &&
          data.elements
            .find((publisher) => publisher.publisher === selectedPublisher)
            ?.elements.find((series) => series.seriesTitle === selectedSeries)
            ?.elements.find((volume) => volume.volumeNumber === selectedVolume)
            ?.elements.length > 0
        );
      }
      return false;
    };

    // Check if there is data at the current level
    if (data && !hasDataAtCurrentLevel()) {
      // If there is no data, navigate back one level
      if (currentLevel === "series") {
        switchLevel("publishers");
      } else if (currentLevel === "volumes") {
        switchLevel("series");
      } else if (currentLevel === "issues") {
        switchLevel("volumes");
      }
    }
  }, [data, currentLevel, selectedPublisher, selectedSeries, selectedVolume]);

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
        "http://localhost:8080/comics/" + selectedIssueId + "/update",
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

  function onEditSubmit(FormData: FormData) {
    setSaveLoading(true);
    setSubmittedData(FormData);

    updateComicMutation.mutate(
      {
        ...FormData,
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

  function handleClick(comidId: number) {
    removeFromCollcetionMutation.mutate(
      {
        comicId: comidId,
      },
      {
        onSuccess: () => {
          refetch();
          return toast({
            title: "Comic removed from personal collection",
            description: "Comic successfully removed from personal collection",
            variant: "success",
          });
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
    return <div>Error loading personal collection</div>;
  }

  if (!data) {
    return <div>No comics in personal collection</div>;
  }

  return (
    <div className="flex gap-10">
      <aside className="mt-10 flex h-full w-1/5 items-center justify-center">
        <div className="flex w-full flex-col gap-5">
          <Accordion
            className="w-full"
            type="single"
            collapsible
            defaultValue="item-2"
          >
            <AccordionItem value="item-1">
              <AccordionTrigger className="font-heading text-xl">
                Search
              </AccordionTrigger>
              <AccordionContent>
                <div className="flex flex-col gap-5">
                  <Form {...form}>
                    <form
                      onSubmit={form.handleSubmit(onSubmit)}
                      className="space-y-8"
                    >
                      <FormField
                        control={form.control}
                        name="queryString"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>Search Term</FormLabel>
                            <FormControl>
                              <Input placeholder="search" {...field} />
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="searchType"
                        render={({ field }) => (
                          <FormItem className="space-y-3">
                            <FormLabel>Search Type</FormLabel>
                            <FormControl>
                              <RadioGroup
                                onValueChange={field.onChange}
                                defaultValue={field.value}
                                className="flex flex-col space-y-1"
                              >
                                <FormItem className="flex items-center space-x-3 space-y-0">
                                  <FormControl>
                                    <RadioGroupItem value="PARTIAL" />
                                  </FormControl>
                                  <FormLabel className="font-normal">
                                    Partial Matching
                                  </FormLabel>
                                </FormItem>
                                <FormItem className="flex items-center space-x-3 space-y-0">
                                  <FormControl>
                                    <RadioGroupItem value="EXACT" />
                                  </FormControl>
                                  <FormLabel className="font-normal">
                                    Exact Matching
                                  </FormLabel>
                                </FormItem>
                              </RadioGroup>
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="sortType"
                        render={({ field }) => (
                          <FormItem className="space-y-3">
                            <FormLabel>Sort Type</FormLabel>
                            <FormControl>
                              <RadioGroup
                                onValueChange={field.onChange}
                                defaultValue={field.value}
                                className="flex flex-col space-y-1"
                              >
                                <FormItem className="flex items-center space-x-3 space-y-0">
                                  <FormControl>
                                    <RadioGroupItem value="ALPHABETICAL" />
                                  </FormControl>
                                  <FormLabel className="font-normal">
                                    Alphabetical
                                  </FormLabel>
                                </FormItem>
                                <FormItem className="flex items-center space-x-3 space-y-0">
                                  <FormControl>
                                    <RadioGroupItem value="DATE" />
                                  </FormControl>
                                  <FormLabel className="font-normal">
                                    Date
                                  </FormLabel>
                                </FormItem>
                              </RadioGroup>
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                      <Button type="submit">Submit</Button>
                    </form>
                  </Form>
                  <Button onClick={() => searchMutation.reset()}>Reset</Button>
                </div>
              </AccordionContent>
            </AccordionItem>
            <AccordionItem value="item-2">
              <AccordionTrigger className="font-heading text-xl">
                {" "}
                Stats{" "}
              </AccordionTrigger>
              <AccordionContent>
                <div className="flex flex-col justify-center gap-5">
                  <Label>Total Number of Issues: {numberOfIssues}</Label>
                  <Label>Total Value: {value}</Label>
                </div>
              </AccordionContent>
            </AccordionItem>
          </Accordion>
        </div>
      </aside>
      <div className="mt-10 flex h-full w-full items-center justify-center">
        <div className="flex flex-col items-center justify-center">
          <h1 className="mb-10 text-4xl font-bold">Personal Collection</h1>
          {searchMutation.isSuccess ? (
            searchMutation.data.length === 0 ? (
              <div className="flex w-full items-center justify-center text-lg">
                No comics found
              </div>
            ) : (
              <div className="grid grid-cols-3 gap-4">
                {searchMutation.data.map((comic) => (
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
                        <div>
                          <div className="flex items-center">
                            <div className="mr-2">{"Vol. "}</div>
                            <div className="mr-2">
                              {comic.volumeNumber + ","}
                            </div>
                          </div>
                          <div className="flex items-center">
                            <div className="mr-2">{"Issue "}</div>
                            <div className="mr-2">{comic.issueNumber}</div>
                          </div>
                        </div>
                        <div>
                          <div className="mr-2">{"Publication Date: "}</div>
                          <div className="mr-2">{comic.publicationDate}</div>
                        </div>
                      </CardFooter>
                    </Card>
                  </Link>
                ))}
              </div>
            )
          ) : data.elements.length === 0 ? (
            <div className="flex flex-col items-center justify-center gap-5">
              <div className="text-center">
                No comics in personal collection
              </div>
              <Button
                onClick={() => {
                  router.push("/");
                }}
              >
                View Comics
              </Button>
            </div>
          ) : (
            <div className="flex w-full items-center justify-center">
              {currentLevel === "publishers" && (
                <div className="flex flex-col items-center justify-center gap-10">
                  <h2 className="font-heading text-3xl">Publishers</h2>
                  <ul className="grid grid-cols-3 items-center justify-center gap-5">
                    {data.elements
                      .sort((a, b) => b.publisher.localeCompare(a.publisher))
                      .map((publisher) => (
                        <li key={publisher.publisher}>
                          <Card
                            className="cursor-pointer"
                            onClick={() => {
                              selectPublisher(publisher.publisher);
                              setNumberOfIssues(publisher.numberOfIssues);
                              setValue(publisher.value);
                            }}
                          >
                            <CardHeader className="text-center">
                              <CardTitle>{publisher.publisher}</CardTitle>
                            </CardHeader>
                          </Card>
                        </li>
                      ))}
                  </ul>
                </div>
              )}

              {currentLevel === "series" && selectedPublisher && (
                <div className="flex flex-col items-center justify-center gap-10">
                  <div className="flex flex-col items-center justify-center gap-5">
                    <h2 className="font-heading text-3xl">
                      Series by {selectedPublisher}
                    </h2>
                    <Button
                      onClick={() => {
                        setCurrentLevel("publishers");
                        setNumberOfIssues(data.numberOfIssues);
                        setValue(data.value);
                      }}
                    >
                      Back
                    </Button>
                  </div>
                  <ul className="grid grid-cols-3 items-center justify-center gap-5">
                    {data.elements
                      .find(
                        (publisher: { publisher: string }) =>
                          publisher.publisher === selectedPublisher,
                      )
                      ?.elements.sort((a, b) =>
                        b.seriesTitle.localeCompare(a.seriesTitle),
                      )
                      .map((series) => (
                        <li key={series.seriesTitle}>
                          <Card
                            className="cursor-pointer"
                            onClick={() => {
                              selectSeries(series.seriesTitle);
                              setNumberOfIssues(series.numberOfIssues);
                              setValue(series.value);
                            }}
                          >
                            <CardHeader className="text-center">
                              <CardTitle>{series.seriesTitle}</CardTitle>
                            </CardHeader>
                          </Card>
                        </li>
                      ))}
                  </ul>
                </div>
              )}

              {currentLevel === "volumes" && selectedSeries && (
                <div className="flex flex-col items-center justify-center gap-10">
                  <div className="flex flex-col items-center justify-center gap-5">
                    <h2 className="font-heading text-3xl">
                      Volumes for Series {selectedSeries} (by{" "}
                      {selectedPublisher})
                    </h2>
                    <Button
                      onClick={() => {
                        setCurrentLevel("series");
                        const publisherCollection = data.elements.find(
                          (item) => item.publisher === selectedPublisher,
                        );
                        setNumberOfIssues(publisherCollection.numberOfIssues);
                        setValue(publisherCollection.value);
                      }}
                    >
                      Back
                    </Button>
                  </div>
                  <ul className="grid grid-cols-3 items-center justify-center gap-5">
                    {data.elements
                      .find(
                        (publisher: { publisher: string }) =>
                          publisher.publisher === selectedPublisher,
                      )
                      ?.elements.find(
                        (series: { seriesTitle: string }) =>
                          series.seriesTitle === selectedSeries,
                      )
                      ?.elements.sort((a, b) =>
                        b.volumeNumber.localeCompare(a.volumeNumber),
                      )
                      .map((volume) => (
                        <li key={volume.volumeNumber}>
                          <Card
                            className="cursor-pointer"
                            onClick={() => {
                              selectVolume(volume.volumeNumber);
                              setNumberOfIssues(volume.numberOfIssues);
                              setValue(volume.value);
                            }}
                          >
                            <CardHeader className="text-center">
                              <CardTitle>{volume.volumeNumber}</CardTitle>
                            </CardHeader>
                          </Card>
                        </li>
                      ))}
                  </ul>
                </div>
              )}

              {currentLevel === "issues" && selectedVolume && (
                <div className="flex flex-col items-center justify-center gap-10">
                  <div className="flex flex-col items-center justify-center gap-5">
                    <h2 className="font-heading text-3xl">
                      Issues for Vol. {selectedVolume} (in Series{" "}
                      {selectedSeries} - by {selectedPublisher})
                    </h2>
                    <Button
                      onClick={() => {
                        setCurrentLevel("volumes");
                        const seriesCollection = data.elements
                          .find((item) => item.publisher === selectedPublisher)
                          ?.elements.find(
                            (item) => item.seriesTitle === selectedSeries,
                          );
                        setNumberOfIssues(seriesCollection.numberOfIssues);
                        setValue(seriesCollection.value);
                      }}
                    >
                      Back
                    </Button>
                  </div>
                  <ul className="grid grid-cols-2 gap-5">
                    {data.elements
                      .find(
                        (publisher: { publisher: string | null }) =>
                          publisher.publisher === selectedPublisher,
                      )
                      ?.elements.find(
                        (series: { seriesTitle: string | null }) =>
                          series.seriesTitle === selectedSeries,
                      )
                      ?.elements.map((issue: { elements: any[] }) =>
                        issue.elements.map((comic) => (
                          <>
                            <li
                              key={comic.elements[0].id}
                              className="flex flex-col items-center justify-center gap-4"
                            >
                              {editing &&
                              selectedIssueId === comic.elements[0].id ? (
                                <form onSubmit={handleSubmit(onEditSubmit)}>
                                  <div className="flex flex-col items-center justify-center gap-10">
                                    <div className="grid grid-cols-2 flex-col items-center justify-center gap-6">
                                      <div className="flex flex-col gap-2">
                                        <Label htmlFor="seriesTitle">
                                          Series Title
                                        </Label>
                                        <Input
                                          id="seriesTitle"
                                          placeholder={
                                            comic.elements[0].seriesTitle
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                          placeholder={
                                            comic.elements[0].storyTitle ?? ""
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                        <Label htmlFor="volumeNumber">
                                          Volume Number
                                        </Label>
                                        <Input
                                          id="volumeNumber"
                                          placeholder={
                                            comic.elements[0].volumeNumber
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                        <Label htmlFor="issueNumber">
                                          Issue Number
                                        </Label>
                                        <Input
                                          id="issueNumber"
                                          placeholder={
                                            comic.elements[0].issueNumber
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                        <Label htmlFor="publisher">
                                          Publisher
                                        </Label>
                                        <Input
                                          id="publisher"
                                          placeholder={
                                            comic.elements[0].publisher ?? ""
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                        <Label htmlFor="description">
                                          Description
                                        </Label>
                                        <Input
                                          id="description"
                                          placeholder={
                                            comic.elements[0].description ?? ""
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                        <Label htmlFor="creators">
                                          Creators
                                        </Label>
                                        <Input
                                          id="creators"
                                          placeholder={
                                            comic.elements[0].creators
                                              ? comic.elements[0].creators
                                                  .length !== 0
                                                ? comic.elements[0].creators
                                                    .map(
                                                      (item: {
                                                        name: string;
                                                      }) => item.name.trim(),
                                                    )
                                                    .join(", ")
                                                : ""
                                              : ""
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                        <Label htmlFor="characters">
                                          Principle Characters
                                        </Label>
                                        <Input
                                          id="characters"
                                          placeholder={
                                            comic.elements[0]
                                              .principleCharacters
                                              ? comic.elements[0]
                                                  .principleCharacters
                                                  .length !== 0
                                                ? comic.elements[0].principleCharacters
                                                    .map(
                                                      (item: {
                                                        name: string;
                                                      }) => item.name.trim(),
                                                    )
                                                    .join(", ")
                                                : ""
                                              : ""
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                        <Label htmlFor="publicationDate">
                                          Publication Date
                                        </Label>
                                        <Input
                                          id="publicationDate"
                                          placeholder={
                                            comic.elements[0].publicationDate
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                          placeholder={
                                            comic.elements[0].value?.toString() ??
                                            "value"
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                          placeholder={
                                            comic.elements[0].grade?.toString() ??
                                            "grade"
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                          placeholder={
                                            comic.elements[0].slabbed?.toString() ??
                                            "slabbed"
                                          }
                                          type="text"
                                          autoCapitalize="none"
                                          disabled={
                                            removeFromCollcetionMutation.isPending ||
                                            saveLoading
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
                                          removeFromCollcetionMutation.isPending ||
                                          saveLoading
                                        }
                                      >
                                        Cancel
                                      </Button>

                                      <Button
                                        disabled={
                                          removeFromCollcetionMutation.isPending ||
                                          saveLoading
                                        }
                                        type="submit"
                                      >
                                        {(removeFromCollcetionMutation.isPending ||
                                          saveLoading) && (
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
                                    <div>{comic.elements[0].seriesTitle}</div>
                                    {comic.elements[0].storyTitle && (
                                      <>
                                        <div>Story Title</div>
                                        <div>
                                          {comic.elements[0].storyTitle}
                                        </div>
                                      </>
                                    )}
                                    <div>Volume Number</div>
                                    <div>{comic.elements[0].volumeNumber}</div>
                                    <div>Issue Number</div>
                                    <div>{comic.elements[0].issueNumber}</div>
                                    {comic.elements[0].description && (
                                      <>
                                        <div>Description</div>
                                        <div>
                                          {comic.elements[0].description}
                                        </div>
                                      </>
                                    )}
                                    {comic.elements[0].creators &&
                                      comic.elements[0].creators.length > 0 && (
                                        <>
                                          <div>Creators</div>
                                          <div>
                                            {comic.elements[0].creators
                                              .map((item: { name: string }) =>
                                                item.name.trim(),
                                              )
                                              .join(", ")}
                                          </div>
                                        </>
                                      )}
                                    {comic.elements[0].principleCharacters &&
                                      comic.elements[0].principleCharacters
                                        .length > 0 && (
                                        <>
                                          <div>Principle Characters</div>
                                          <div>
                                            {comic.elements[0].principleCharacters
                                              .map((item: { name: string }) =>
                                                item.name.trim(),
                                              )
                                              .join(", ")}
                                          </div>
                                        </>
                                      )}
                                    <div>Publication Date</div>
                                    <div>
                                      {comic.elements[0].publicationDate}
                                    </div>
                                    {comic.elements[0].value !== 0 && (
                                      <>
                                        <div>Value</div>
                                        <div>
                                          {comic.elements[0].value?.toString()}
                                        </div>
                                      </>
                                    )}
                                    {comic.elements[0].grade !== 0 && (
                                      <>
                                        <div>Grade</div>
                                        <div>
                                          {comic.elements[0].grade?.toString()}
                                        </div>
                                      </>
                                    )}
                                    <div>Slabbed</div>
                                    <div>
                                      {comic.elements[0].slabbed?.toString() ??
                                        "slabbed"}
                                    </div>
                                  </div>
                                </div>
                              )}
                              {editing ? (
                                <></>
                              ) : (
                                <div className="flex flex-col items-center justify-center gap-5">
                                  <Button
                                    onClick={() => {
                                      setSelectedIssueId(comic.elements[0].id);
                                      reset({
                                        publisher: comic.elements[0]?.publisher,
                                        seriesTitle:
                                          comic.elements[0]?.seriesTitle,
                                        storyTitle:
                                          comic.elements[0]?.storyTitle,
                                        volumeNumber:
                                          comic.elements[0]?.volumeNumber,
                                        issueNumber:
                                          comic.elements[0]?.issueNumber,
                                        description:
                                          comic.elements[0]?.description,
                                        creators: comic.elements[0]?.creators
                                          ? comic.elements[0]?.creators
                                              .map((item) => item.name.trim())
                                              .join(", ")
                                          : "",
                                        principleCharacters: comic.elements[0]
                                          ?.principleCharacters
                                          ? comic.elements[0]?.principleCharacters
                                              .map((item) => item.name.trim())
                                              .join(", ")
                                          : "",
                                        publicationDate:
                                          comic.elements[0]?.publicationDate,
                                        value:
                                          comic.elements[0]?.value?.toString() ??
                                          "",
                                        grade:
                                          comic.elements[0]?.grade?.toString() ??
                                          "",
                                        slabbed:
                                          comic.elements[0]?.slabbed?.toString() ??
                                          "",
                                      });
                                      setEditing((editing) => !editing);
                                    }}
                                  >
                                    Edit
                                  </Button>
                                  <Button
                                    variant={"destructive"}
                                    onClick={() =>
                                      handleClick(comic.elements[0].id)
                                    }
                                    disabled={
                                      removeFromCollcetionMutation.isPending ||
                                      saveLoading
                                    }
                                  >
                                    {removeFromCollcetionMutation.isPending && (
                                      <Icons.spinner className="mr-2 h-4 w-4 animate-spin" />
                                    )}
                                    Remove from Personal Collection
                                  </Button>
                                </div>
                              )}
                            </li>
                          </>
                        )),
                      )}
                  </ul>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
