import React, { ChangeEvent, useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";
import { useParams } from "react-router-dom";
import DocumentsTable from "../components/DocumentsTable";
import Modal from "../components/modal/Modal";
import { IDocument } from "../types/document";
import { AxiosResponse } from "axios";
import { deleteDocument } from "../services/document.service";
interface DirectoryPageProps {
  fetchDocuments: (userId: Number, dirId: Number) => Promise<IDocument[]>;
  deleteDocument: typeof deleteDocument;
  uploadDocument: (
    userId: Number,
    dirId: Number,
    file: File
  ) => Promise<AxiosResponse<any, any>>;
}
const DocumentsPage: React.FC<DirectoryPageProps> = ({
  fetchDocuments,
  uploadDocument,
  deleteDocument,
}) => {
  const { directoryId } = useParams<{ directoryId: string }>();
  const [documents, setDocuments] = useState<IDocument[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openModal, setOpenModal] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const fetchDocumentsHandler = async () => {
    try {
      setError(null);
      setLoading(true);
      const data = await fetchDocuments(1, Number(directoryId));
      setDocuments(data);
    } catch (error: any) {
      console.error(error);
      setError(error.message || "Failed to fetch documents");
    } finally {
      setLoading(false);
    }
  };
  const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files.length > 0) {
      setSelectedFile(event.target.files[0]);
    }
  };
  const uploadBtnHandler = async () => {
    try {
      const res = await uploadDocument(1, Number(directoryId), selectedFile!);
      console.log(res);
    } catch (e: any) {
      alert(e);
    }
  };
  useEffect(() => {
    fetchDocumentsHandler();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className=" bg-slate-50 p-1">
      <Modal
        isOpen={openModal}
        onClose={() => {
          setOpenModal(false);
        }}
      >
        <div className="flex flex-col space-y-4">
          <h1 className="text-xl font-semibold">Add New Directory</h1>
          <label htmlFor="name" className="text-sm">
            File
          </label>
          <input
            type="file"
            name="file"
            onChange={handleFileChange}
            className="border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="select file to upload"
          />

          <button
            className="bg-blue-500 text-white rounded-md p-2 hover:bg-blue-600 transition duration-300"
            onClick={uploadBtnHandler}
          >
            Add
          </button>
        </div>
      </Modal>
      <div className="flex justify-end">
        <button
          onClick={() => {
            deleteDocument(1, Number(directoryId), documents[0].name);
          }}
        >
          test
        </button>
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
      <div className=" flex flex-row min-h-screen  mt-1 bg-gray-100 ">
        <div className="pl-2 basis-1/6 bg-slate-50 ">
          <button
            className="  bg-orange-400 border-grey-400 shadow-md hover:bg-orange-500 hover:shadow-sm border-2 p-4 rounded-full "
            onClick={() => {
              setOpenModal(true);
            }}
          >
            <div className="flex items-center justify-center">
              <FaPlus className="mr-1" style={{ color: "white" }} />
              <h3> Upload </h3>
            </div>
          </button>
          <hr className="mt-4"></hr>
        </div>
        <div className=" basis-5/6 rounded-lg border-gray-200 border-2 bg-white p-1">
          <DocumentsTable documents={documents}></DocumentsTable>
        </div>
      </div>
    </div>
  );
};

export default DocumentsPage;
