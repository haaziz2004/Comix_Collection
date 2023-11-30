import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

// This function can be marked `async` if using `await` inside
export function middleware(request: NextRequest) {
  // check request for id cookie
  // if not found, redirect to login

  const path = request.nextUrl.pathname;
  const isAuthPage = path.startsWith("/login") || path.startsWith("/register");
  const isAuth = request.cookies.get("user-id") !== undefined;

  if (isAuthPage) {
    if (isAuth) {
      return NextResponse.redirect(new URL("/", request.url));
    }

    return null;
  }

  if (!isAuth) {
    let from = path;
    if (request.nextUrl.search) {
      from += request.nextUrl.search;
    }

    return NextResponse.redirect(
      new URL(`/login?from=${encodeURIComponent(from)}`, request.url),
    );
  }
}

// See "Matching Paths" below to learn more
export const config = {
  matcher: [
    "/account",
    "/login",
    "/register",
    "/collection",
    "/collection/:path*",
  ],
};
