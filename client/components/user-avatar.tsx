import { type AvatarProps } from "@radix-ui/react-avatar";

import { Icons } from "@/components/icons";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { type userSchema } from "@/lib/validations/user";
import { type z } from "zod";

type user = z.infer<typeof userSchema>;
interface UserAvatarProps extends AvatarProps {
  user: Pick<user, "username">;
}

// TODO: Set priority on images to make sure they load first
export function UserAvatar({ user, ...props }: UserAvatarProps) {
  return (
    <Avatar {...props}>
      <AvatarFallback>
        <span className="sr-only">{user.username}</span>
        <Icons.user className="h-4 w-4" />
      </AvatarFallback>
    </Avatar>
  );
}
