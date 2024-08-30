import React, { useState } from "react";
import TableComponent from "../components/TableComponent";
function HomePage({}) {
  const [data, setData] = useState([]);

  const getDirectories = async () => {
    const res = await fetch("http://localhost:8080/api/v1/users/1/workspace/directories", {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    console.log(res.status);
    console.log(res);
  };

  getDirectories();
  return (
    <div className=" bg-slate-50 p-1">
      <div className="flex justify-end">
        <input
          type="search"
          placeholder="search"
          name="search"
          id="search"
          title="seacrh"
          required
          className="max-w-32 mt-1 block w-full px-3 py-2 border border-gray-300 rounded-xl shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        ></input>
      </div>
      <div className=" flex flex-row  mt-1 bg-gray-100 rounded-lg border border-grey-300">
        <div className="basis-1/4 bg-rose-400">TEST</div>
        <TableComponent className="" data={data} />
      </div>
    </div>
  );
}

export default HomePage;
