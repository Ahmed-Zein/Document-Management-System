import React from "react";
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from "react-router-dom";
import HomePage from "./pages/HomePage"; // Ensure these components are imported
import LoginPage from "./pages/LoginPage";
const App = () => {
  const loginHandler = async (body) => {
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
  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path="/">
        <Route index element={<HomePage />} />
        <Route
          path="login"
          element={<LoginPage loginHandler={loginHandler} />}
        />
        <Route path="test" element={<LoginPage />} />
        {/* <Route path="*" element={<NotFoundPage />} /> */}
      </Route>
    )
  );

  return <RouterProvider router={router} />;
};

export default App;
