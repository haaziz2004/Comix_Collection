import {
  CardTitle,
  CardDescription,
  CardHeader,
  CardContent,
  Card,
} from "@/components/ui/card";
import { type RouterOutputs } from "@/trpc/shared";

interface ComicProps {
  comic: RouterOutputs["comic"]["getAll"][0];
}

export function Comic({ comic }: ComicProps) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>{comic.seriesTitle}</CardTitle>
        <CardDescription>Publisher: {comic.publisher}</CardDescription>
      </CardHeader>
      <CardContent>
        <p className="text-sm/relaxed">
          Volume: {comic.volumeNumber}, Issue: {comic.issueNumber}
        </p>
        <p className="text-sm/relaxed">
          Publication Date:{" "}
          {comic.publicationDate.toLocaleDateString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
          })}
        </p>
        <p className="text-sm/relaxed">
          Story Title: {comic.storyTitle ? comic.storyTitle : "No Title"}
        </p>
      </CardContent>
    </Card>
  );
}
