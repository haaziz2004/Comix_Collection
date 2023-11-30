import { cookies } from "next/headers";

export async function POST(request: Request) {
  const data = await request.json();
  const signInResult = await fetch("http://localhost:8080/users/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!signInResult.ok) {
    switch (signInResult.status) {
      case 401:
        return new Response("Invalid username or password", {
          status: 401,
        });
      default:
        return new Response("Login Failed", {
          status: 500,
        });
    }
  }

  const { id } = await signInResult.json();

  cookies().set("user-id", id, {
    maxAge: 3600,
    expires: new Date(Date.now() + 3600000),
    httpOnly: true,
    domain: "localhost",
    path: "/",
  });

  return new Response("Login Successful", {
    status: 200,
  });
}
