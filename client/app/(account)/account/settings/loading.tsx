import { CardSkeleton } from "@/components/card-skeleton";
import { AccountHeader } from "@/components/header";
import { AccountShell } from "@/components/shell";

export default function AccountSettingsLoading() {
  return (
    <AccountShell>
      <AccountHeader
        heading="Settings"
        text="Manage account and website settings."
      />
      <div className="grid gap-10">
        <CardSkeleton />
      </div>
    </AccountShell>
  );
}
