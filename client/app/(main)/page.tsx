"use client";

import ComicList from "@/components/comic-list";
import { Search } from "@/components/search";
import { api } from "@/trpc/react";

export default function Home() {
  const {
    data: comics,
    isLoading,
    isError,
  } = api.comic.getAll.useQuery(undefined, {
    refetchOnMount: false,
    refetchOnReconnect: false,
    refetchOnWindowFocus: false,
    staleTime: Infinity,
  });

  const searchMutation = api.comic.search.useMutation();

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
    <div className="flex gap-10">
      <aside className="mt-10 flex h-full w-1/5 items-center justify-center">
        <Search searchMutation={searchMutation} />
      </aside>
      {searchMutation.isLoading ? (
        <div className="flex w-full items-center justify-center text-lg">
          Searching...
        </div>
      ) : searchMutation.isSuccess ? (
        searchMutation.data.length === 0 ? (
          <div className="flex w-full items-center justify-center text-lg">
            No comics found
          </div>
        ) : (
          <ComicList
            heading="Search Results"
            subheading="Explore your search results."
            comics={searchMutation.data}
          />
        )
      ) : (
        <ComicList
          heading="Comics"
          subheading="Explore our vast collection of comics."
          comics={comics}
        />
      )}
    </div>
  );
}
