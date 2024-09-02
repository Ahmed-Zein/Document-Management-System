import axios from "axios";
import React, { Key } from "react";
import { FaLock, FaLockOpen } from "react-icons/fa6";
import { useNavigate } from "react-router-dom";
import { toggleVisibility } from "../services/directory.service";
import { Directory } from "../types/directory";
import FolderIcon from "./FolderIcon";

interface TableComponentPropst {
  data: Directory[];
  toggelVisibility: (directory: Directory) => void;
}

const TableComponent: React.FC<TableComponentPropst> = ({ data }) => {
  const navigate = useNavigate();
  const headers = ["Name", "Created at", "Owner", "Public"];
  const IToggelVisiblity = async (dir: Directory) => {
    toggleVisibility(dir);
  };

  const onClickHandler = async (id: Number) => {
    try {
      await axios.get(
        `http://localhost:8080/api/v1/users/1/workspace/directories/${id}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      navigate("/documents");
    } catch (e) {
      console.log(e);
    }
  };
  const options: Intl.DateTimeFormatOptions = {
    year: "numeric",
    month: "numeric",
    day: "numeric",
  };
  return (
    <div className="overflow-x-auto ">
      <table className="min-w-full bg-white text-left rounded-lg shadow-md border border-gray-300">
        <thead className="bg-gray-100">
          <tr>
            {headers.map((header) => (
              <th
                key={header}
                className="py-2 px-4 border-b text-gray-600 text-left"
              >
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((dir) => (
            <tr
              key={dir.id as Key}
              className="hover:bg-gray-50 cursor-pointer transition-colors duration-200"
            >
              <td
                className="py-2 px-4 flex items-center space-x-2"
                onClick={() => onClickHandler(dir.id!)}
              >
                <FolderIcon />
                <p className="text-gray-700">{dir.name}</p>
              </td>
              <td className="py-2 px-4 text-gray-600">
                {new Date(dir.createdAt!).toLocaleString("en-US", options)}
              </td>
              <td className="py-2 px-4 text-gray-600">
                {dir.localUser?.email}
              </td>
              <td className="py-2 px-4" onClick={() => IToggelVisiblity(dir)}>
                {dir.isPublic ? (
                  <FaLock
                    className="min-h-5 min-w-5"
                    style={{ color: "#FF204E" }}
                  ></FaLock>
                ) : (
                  <FaLockOpen
                    className="min-h-5 min-w-5"
                    style={{ color: "#399918" }}
                  ></FaLockOpen>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TableComponent;
