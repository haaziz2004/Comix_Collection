import { cookies } from "next/headers";

export async function POST(request: Request) {
  const data = await request.json();
  const registerResult = await fetch("http://localhost:8080/users/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!registerResult.ok) {
    switch (registerResult.status) {
      case 409:
        return new Response("Username already exists", {
          status: 409,
        });
      default:
        return new Response("Sign Up Failed", {
          status: 500,
        });
    }
  }

  const { id } = await registerResult.json();

  cookies().set("userId", id, {
    maxAge: 3600,
    expires: new Date(Date.now() + 3600000),
    httpOnly: true,
    domain: "localhost",
    path: "/",
  });

  return new Response("Sign Up Successful", {
    status: 200,
  });
}
