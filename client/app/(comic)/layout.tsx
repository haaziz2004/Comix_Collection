import { MainNav } from "@/components/main-nav";
import { homepageConfig } from "@/config/homepage";
import { getCurrentUser } from "@/lib/session";

interface ComicPageLayoutProps {
  children: React.ReactNode;
}

export default async function ComicPageLayout({
  children,
}: ComicPageLayoutProps) {
  const user = await getCurrentUser();

  return (
    <div className="flex min-h-screen flex-col">
      <header className="top-0 z-40 w-full border-b bg-background">
        <MainNav items={homepageConfig.mainNav} user={user} />
      </header>
      <main className="container flex-1">{children}</main>
    </div>
  );
}
