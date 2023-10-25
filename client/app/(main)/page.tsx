"use client";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "@/components/ui/card";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { comicSchema } from "@/lib/validations/comic";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery } from "@tanstack/react-query";
import Link from "next/link";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";

const formSchema = z.object({
  queryString: z.string().min(1),
  searchType: z.enum(["EXACT", "PARTIAL"]),
  sortType: z.enum(["ALPHABETICAL", "DATE"]),
});

export default function Home() {
  const {
    data: comics,
    isLoading,
    isError,
  } = useQuery({
    queryKey: ["comics"],
    queryFn: async () => {
      const data = await fetch("http://localhost:8080/comics/all");

      if (!data.ok) {
        throw new Error("Error fetching comics");
      }

      return comicSchema.array().parse(await data.json());
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

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError) {
    return <div>Error loading comics</div>;
  }

  if (!comics) {
    return <div>No comics found</div>;
  }

  return (
    <div className="flex">
      <aside className="mt-10 flex h-full w-1/5 items-center justify-center">
        <div className="flex flex-col gap-5">
          <div className="font-heading text-xl">Search</div>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
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
                          <FormLabel className="font-normal">Date</FormLabel>
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
      </aside>
      <div className="mt-10 flex h-full w-full items-center justify-center">
        <div className="flex flex-col items-center justify-center">
          <h1 className="mb-10 text-4xl font-bold">Comics</h1>
          <div className="grid grid-cols-3 gap-4">
            {searchMutation.isSuccess ? (
              searchMutation.data.length === 0 ? (
                <div className="flex w-full items-center justify-center text-lg">
                  No comics found
                </div>
              ) : (
                searchMutation.data.map((comic) => (
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
                ))
              )
            ) : (
              comics.map((comic) => (
                <Link href={`/comic/${comic.id}`} key={comic.id}>
                  <Card className="">
                    <CardHeader className="flex items-center justify-center font-bold">
                      {comic.seriesTitle}
                    </CardHeader>
                    <CardContent className="text-center">
                      <div className="line-clamp-3">
                        {comic.storyTitle ? comic.storyTitle : "No Title"}
                      </div>
                    </CardContent>
                    <CardFooter className="flex flex-col items-center justify-center">
                      <div className="flex gap-2">
                        <div className="flex items-center">
                          <div className="mr-2">{"Vol. "}</div>
                          <div className="mr-2">{comic.volumeNumber + ","}</div>
                        </div>
                        <div className="flex items-center">
                          <div className="mr-2">{"Issue "}</div>
                          <div className="mr-2">{comic.issueNumber}</div>
                        </div>
                      </div>
                      <div className="flex items-center">
                        <div className="mr-2">{"Published: "}</div>
                        <div className="mr-2">{comic.publicationDate}</div>
                      </div>
                    </CardFooter>
                  </Card>
                </Link>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
