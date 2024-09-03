export const loginHandler = async (body: object) => {
  const res = await fetch("http://localhost:8080/api/v1/auth/login", {
    method: "POST",
    headers: {
      "content-type": "application/json",
    },
    body: JSON.stringify(body),
  });
  if (res.status > 300) {
    throw new Error(`got: ${res.status}, during login`);
  }
  const json = await res.json();
  localStorage.setItem("token", json.jwt);
};
