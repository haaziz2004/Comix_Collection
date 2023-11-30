import { type RouterOutputs } from "@/trpc/shared";
import Link from "next/link";
import { Comic } from "./comic";

interface ComicListProps {
  comics: RouterOutputs["comic"]["getAll"];
  heading: string;
  subheading: string;
}

export default function ComicList({
  comics,
  heading,
  subheading,
}: ComicListProps) {
  return (
    <section className="w-full py-12 md:py-24 lg:py-32">
      <div className="container grid items-start justify-center gap-4 px-4 text-center md:px-6 lg:gap-10">
        <div className="space-y-3">
          <h2 className="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">
            {heading}
          </h2>
          <p className="mx-auto max-w-[700px] text-zinc-500 dark:text-zinc-400 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed">
            {subheading}
          </p>
        </div>
        <div className="grid w-full grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
          {comics.map((comic) => (
            <Link href={`/comic/${comic.id}`} key={comic.id}>
              <Comic comic={comic} />
            </Link>
          ))}
        </div>
      </div>
    </section>
  );
}
