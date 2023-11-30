import { Button } from "@/components/ui/button";
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
import { type api } from "@/trpc/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { useSearchParams, useRouter } from "next/navigation";
import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";

interface SearchProps {
  searchMutation: ReturnType<typeof api.comic.search.useMutation>;
}

const formSchema = z.object({
  queryString: z.string().min(1),
  searchType: z.enum(["EXACT", "PARTIAL"]),
  sortType: z.enum(["ALPHABETICAL", "DATE"]),
});

export function Search({ searchMutation }: SearchProps) {
  const router = useRouter();
  const searchParams = useSearchParams();

  useEffect(() => {
    if (searchParams.get("q")) {
      searchMutation.mutate({
        queryString: searchParams.get("q") ?? "",
        searchType: searchParams.get("search") ?? "PARTIAL",
        sortType: searchParams.get("sort") ?? "ALPHABETICAL",
      });
    }
  }, []);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      queryString: searchParams?.get("q") ?? "",
      searchType: searchParams?.get("search") ?? "PARTIAL",
      sortType: searchParams?.get("sort") ?? "ALPHABETICAL",
    },
    mode: "onSubmit",
    reValidateMode: "onSubmit",
  });

  function onSubmit(values: z.infer<typeof formSchema>) {
    const newParams = new URLSearchParams(searchParams.toString());

    newParams.set("q", values.queryString);
    newParams.set("search", values.searchType);
    newParams.set("sort", values.sortType);

    router.push(`?${newParams.toString()}`);
    searchMutation.mutate(values);
  }

  return (
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
      <Button
        variant={"destructive"}
        onClick={() => {
          searchMutation.reset();
          form.reset({
            queryString: "",
            searchType: "PARTIAL",
            sortType: "ALPHABETICAL",
          });
          // clear search params
          router.replace("?");
        }}
      >
        Reset
      </Button>
    </div>
  );
}
