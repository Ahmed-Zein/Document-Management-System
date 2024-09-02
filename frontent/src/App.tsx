import axios from "axios";
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from "react-router-dom";
import DirectoryPage from "./pages/DocumentsPage";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import NotFoundPage from "./pages/NotFoundPage";
import {
  addNewDirectory,
  toggleVisibility,
} from "./services/directory.service";
import { Document } from "./types/document";
import ProtectedRoute from "./components/ProtectedRoute";

const App = () => {
  const loginHandler = async (body: object) => {
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

  const fetchDirectories = async (userId: Number) => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/${userId}/workspace/directories`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    return response.data;
  };
  const fetchDocuments = async (
    userId: Number,
    dirId: Number
  ): Promise<Document[]> => {
    const response = await axios.get(
      `http://localhost:8080/api/v1/users/${userId}/workspace/directories/${dirId}/documents`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    return response.data as Document[];
  };

  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path="/">
        <Route
          index
          element={
            <ProtectedRoute>
              <HomePage
                fetchDirectories={fetchDirectories}
                addNewDirectory={addNewDirectory}
                toggleVisibility={toggleVisibility}
              />
            </ProtectedRoute>
          }
        />
        <Route
          path="documents"
          element={
            <ProtectedRoute>
              <DirectoryPage fetchDocuments={fetchDocuments} />
            </ProtectedRoute>
          }
        />
        <Route
          path="login"
          element={<LoginPage loginHandler={loginHandler} />}
        />
        <Route
          path="test"
          element={<LoginPage loginHandler={loginHandler} />}
        />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    )
  );

  return <RouterProvider router={router} />;
};

export default App;
