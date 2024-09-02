import React from "react";
import { Link } from 'react-router-dom';

function NotFoundPage() {
  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gradient-to-br from-slate-50 to-indigo-100-400 text-gray-800">
      <h1 className="text-6xl font-bold text-red-600 mb-4">Oops!</h1>
      <p className="text-xl mb-6">404 - Page Not Found</p>
      <Link
        to="/"
        className="px-4 py-2 bg-indigo-600 text-white text-lg rounded-lg shadow-lg hover:bg-indigo-50-700 transition duration-300"
      >
        Go Back to Home
      </Link>
    </div>
  );
}

export default NotFoundPage;
