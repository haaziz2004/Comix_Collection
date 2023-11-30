/**
 * This code was generated by v0 by Vercel.
 * @see https://v0.dev/t/WH9NwRbxmoI
 */
import {
  CardTitle,
  CardDescription,
  CardHeader,
  CardContent,
  Card,
} from "@/components/ui/card";

export function MainCollection() {
  return (
    <section className="w-full py-12 md:py-24 lg:py-32">
      <div className="container grid items-start justify-center gap-4 px-4 text-center md:px-6 lg:gap-10">
        <div className="space-y-3">
          <h2 className="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">
            Comic Collection
          </h2>
          <p className="mx-auto max-w-[700px] text-zinc-500 dark:text-zinc-400 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed">
            Explore our vast collection of comics.
          </p>
        </div>
        <div className="grid w-full grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
          <Card>
            <CardHeader>
              <CardTitle>Superman: The Man of Steel</CardTitle>
              <CardDescription>Publisher: DC Comics</CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-sm/relaxed">Volume: 1, Issue: 1</p>
              <p className="text-sm/relaxed">Publication Date: April 1986</p>
              <p className="text-sm/relaxed">Story Title: The Legend Begins</p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader>
              <CardTitle>Batman: Year One</CardTitle>
              <CardDescription>Publisher: DC Comics</CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-sm/relaxed">Volume: 1, Issue: 404</p>
              <p className="text-sm/relaxed">Publication Date: March 1987</p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader>
              <CardTitle>The Amazing Spider-Man</CardTitle>
              <CardDescription>Publisher: Marvel Comics</CardDescription>
            </CardHeader>
            <CardContent>
              <p className="text-sm/relaxed">Volume: 1, Issue: 15</p>
              <p className="text-sm/relaxed">Publication Date: August 1964</p>
              <p className="text-sm/relaxed">Story Title: Kraven the Hunter!</p>
            </CardContent>
          </Card>
        </div>
      </div>
    </section>
  );
}