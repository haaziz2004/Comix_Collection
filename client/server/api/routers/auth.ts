import { z } from "zod";

import { createTRPCRouter, publicProcedure } from "@/server/api/trpc";
import { Prisma } from "@prisma/client";
import { TRPCError } from "@trpc/server";
import { cookies } from "next/headers";
import { eq, and, DrizzleError } from "drizzle-orm";
import { users } from "@/server/db/schema";
import { loginSchema, registerSchema } from "@/lib/schemas";

export const authRouter = createTRPCRouter({
  login: publicProcedure
    .input(z.object({ username: z.string(), password: z.string() }))
    .mutation(async ({ ctx, input }) => {
      // const user = await ctx.db.query.users.findFirst({
      //   where: and(
      //     eq(users.username, input.username),
      //     eq(users.password, input.password),
      //   ),
      // });

      // if (!user) {
      //   throw new TRPCError({
      //     code: "UNAUTHORIZED",
      //     message: "Invalid username or password",
      //   });
      // }

      // cookies().set("user-id", user.id.toString(), {
      //   maxAge: 3600,
      //   expires: new Date(Date.now() + 3600000),
      //   httpOnly: true,
      //   domain: "localhost",
      //   path: "/",
      // });

      // return user;

      const response = await fetch("http://localhost:8080/api/users/login", {
        method: "POST",
        body: JSON.stringify(input),
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        switch (response.status) {
          case 401:
            throw new TRPCError({
              code: "UNAUTHORIZED",
              message: "Invalid username or password",
            });
          default:
            throw new TRPCError({
              code: "INTERNAL_SERVER_ERROR",
              message: "Internal server error",
            });
        }
      }

      const user = loginSchema.parse(await response.json());

      cookies().set("user-id", user.id.toString(), {
        maxAge: 3600,
        expires: new Date(Date.now() + 3600000),
        httpOnly: true,
        domain: "localhost",
        path: "/",
      });

      return user;
    }),

  register: publicProcedure
    .input(z.object({ username: z.string(), password: z.string() }))
    .mutation(async ({ ctx, input }) => {
      // const user = await ctx.db.query.users.findFirst({
      //   where: eq(users.username, input.username),
      // });

      // if (user) {
      //   throw new TRPCError({
      //     code: "CONFLICT",
      //     message: "Username already exists",
      //   });
      // }

      // const newUser = await ctx.db.insert(users).values({
      //   username: input.username,
      //   password: input.password,
      // });

      // cookies().set("user-id", newUser.insertId, {
      //   maxAge: 3600,
      //   expires: new Date(Date.now() + 3600000),
      //   httpOnly: true,
      //   domain: "localhost",
      //   path: "/",
      // });

      // return user;

      const response = await fetch("http://localhost:8080/api/users/register", {
        method: "POST",
        body: JSON.stringify(input),
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        switch (response.status) {
          case 409:
            throw new TRPCError({
              code: "CONFLICT",
              message: "Username already exists",
            });
          default:
            throw new TRPCError({
              code: "INTERNAL_SERVER_ERROR",
              message: "Internal server error",
            });
        }
      }

      const userId = registerSchema.parse(await response.json());

      cookies().set("user-id", userId.toString(), {
        maxAge: 3600,
        expires: new Date(Date.now() + 3600000),
        httpOnly: true,
        domain: "localhost",
        path: "/",
      });

      return userId;
    }),
});
