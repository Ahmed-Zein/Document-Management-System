import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
  RouterProvider,
} from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import DocumentsPage from "./pages/DocumentsPage";
import LoginPage from "./pages/LoginPage";
import NotFoundPage from "./pages/NotFoundPage";
import { loginHandler } from "./services/auth.service";
import {
  addNewDirectory,
  fetchDirectories,
  toggleVisibility,
} from "./services/directory.service";
import {
  deleteDocument,
  fetchDocuments,
  uploadDocument,
} from "./services/document.service";
import DirectoriesPage from "./pages/DirectoriesPage";

const App = () => {
  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path="/">
        <Route
          index
          element={
            <ProtectedRoute>
              <DirectoriesPage
                fetchDirectories={fetchDirectories}
                addNewDirectory={addNewDirectory}
                toggleVisibility={toggleVisibility}
              />
            </ProtectedRoute>
          }
        />
        <Route
          path="/directories/:directoryId/documents"
          element={
            <ProtectedRoute>
              <DocumentsPage
                fetchDocuments={fetchDocuments}
                uploadDocument={uploadDocument}
                deleteDocument={deleteDocument}
              />
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
