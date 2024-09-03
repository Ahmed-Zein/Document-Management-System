import axios from "axios";
import React, { Key } from "react";
import { FaFilePdf } from "react-icons/fa6";
import { IDocument } from "../types/document";
import DocumentIcon from "./DocumentIcon";

interface TableComponentPropst {
  documents: IDocument[];
}

const DocumentsTable: React.FC<TableComponentPropst> = ({ documents }) => {
  const headers = ["Name", "Created at", "Type"];

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
    } catch (e) {
      alert(e);
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
          {documents.map((doc) => (
            <tr
              key={doc.id as Key}
              className="hover:bg-gray-50 cursor-pointer transition-colors duration-200"
            >
              <td
                className="py-2 px-4 flex items-center space-x-2"
                onClick={() => onClickHandler(doc.id!)}
              >
                <DocumentIcon
                  className="h-7 w-7"
                  type={doc.contentType}
                ></DocumentIcon>
                <p className="text-gray-700">{doc.name}</p>
              </td>
              <td className="py-2 px-4 text-gray-600">
                {new Date(doc.createdAt!).toLocaleString("en-US", options)}
              </td>
              <td className="py-2 px-4 text-gray-600">
                {doc.contentType.split("/")[1] || ""}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DocumentsTable;
