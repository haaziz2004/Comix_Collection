"use client";

import Link from "next/link";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { UserAvatar } from "@/components/user-avatar";

import { cn } from "@/lib/utils";
import { usePathname } from "next/navigation";
import { Icons } from "./icons";
import { type z } from "zod";
import { type userSchema } from "@/lib/validations/user";
import { signOut } from "@/lib/session";
import { useRouter } from "next/navigation";

type user = z.infer<typeof userSchema>;
interface UserAccountNavProps {
  user: Pick<user, "username">;
}

export function UserAccountNav({ user }: UserAccountNavProps) {
  const path = usePathname();
  const router = useRouter();

  return (
    <DropdownMenu>
      <DropdownMenuTrigger>
        <UserAvatar
          user={{ username: user.username ?? null }}
          className="h-8 w-8"
        />
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        <div className="flex items-center justify-start gap-2 p-2">
          <div className="flex flex-col space-y-1 leading-none">
            {user.username && <p className="font-medium">{user.username}</p>}
          </div>
        </div>
        <DropdownMenuSeparator />

        <DropdownMenuItem
          className="cursor-pointer"
          onSelect={(event: Event) => {
            event.preventDefault();
            // void signOut({
            //   callbackUrl: `${window.location.origin}/login`,
            // });
            void signOut();
            router.push("/login");
          }}
        >
          Sign out
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
