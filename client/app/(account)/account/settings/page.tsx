import { redirect } from "next/navigation";

import { AccountHeader } from "@/components/header";
import { AccountShell } from "@/components/shell";
import { UserNameForm } from "@/components/user-name-form";
import { authOptions } from "@/server/auth";
import { getCurrentUser } from "@/lib/session";

export const metadata = {
  title: "Settings",
  description: "Manage account and website settings.",
};

export default async function SettingsPage() {
  const user = await getCurrentUser();

  if (!user) {
    redirect(authOptions?.pages?.signIn ?? "/login");
  }

  return (
    <AccountShell>
      <AccountHeader
        heading="Settings"
        text="Manage account and website settings."
      />
      <div className="grid gap-10">
        <UserNameForm user={{ id: user.id, name: user.name ?? "" }} />
      </div>
    </AccountShell>
  );
}
