import { MainNav } from "@/components/main-nav";
import { AccountNav } from "@/components/account-sidebar-nav";
import { accountConfig } from "@/config/account";
// import { getCurrentUser } from "@/lib/session";

interface AccountLayoutProps {
  children?: React.ReactNode;
}

export default function AccountLayout({ children }: AccountLayoutProps) {
  // const user = await getCurrentUser();

  return (
    <div className="flex min-h-screen flex-col space-y-6">
      <header className="sticky top-0 z-40 border-b bg-background">
        <MainNav
          items={accountConfig.mainNav}
          //  user={user}
        />
      </header>
      <div className="container grid flex-1 gap-12 md:grid-cols-[200px_1fr]">
        <aside className="hidden w-[200px] flex-col md:flex">
          <AccountNav items={accountConfig.sidebarNav} />
        </aside>
        <main className="flex w-full flex-1 flex-col overflow-hidden">
          {children}
        </main>
      </div>
    </div>
  );
}
