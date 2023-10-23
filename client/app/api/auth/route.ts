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
    return new Response("Invalid credentials", {
      status: 401,
    });
  }

  const { id } = await signInResult.json();

  return new Response("Login Successful", {
    status: 200,
    headers: { "Set-Cookie": `userId=${id}` },
  });
}
