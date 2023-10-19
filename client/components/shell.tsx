import * as React from "react";

import { cn } from "@/lib/utils";

interface AccountShellProps extends React.HTMLAttributes<HTMLDivElement> {}

export function AccountShell({
  children,
  className,
  ...props
}: AccountShellProps) {
  return (
    <div className="flex items-start justify-center md:justify-start">
      <div className={cn("grid grow items-start gap-8", className)} {...props}>
        {children}
      </div>
    </div>
  );
}
