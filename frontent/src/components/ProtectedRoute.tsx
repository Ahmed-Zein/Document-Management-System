import React from "react";
import { Navigate } from "react-router-dom";

interface ProtectedRouteProps {
  children: JSX.Element | JSX.Element[] | string;
}
const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const isAuthenticated = localStorage.getItem("token") != null;
  return isAuthenticated ? (
    <>{children}</>
  ) : (
    <Navigate
      to={{
        pathname: "/login",
      }}
    />
  );
};

export default ProtectedRoute;
