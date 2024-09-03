import React, { useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";
import Modal from "../components/modal/Modal";
import DirectoriesTable from "../components/DirectoriesTable";
import { Directory } from "../types/directory";
import { deleteDirectory } from "../services/directory.service";

interface HomePageProps {
  fetchDirectories: (dirId: Number) => Promise<Directory[]>;
  addNewDirectory: (name: String, isPublic: boolean) => any;
  toggleVisibility: (directory: Directory) => void;
}

const DirectoriesPage: React.FC<HomePageProps> = ({
  fetchDirectories,
  addNewDirectory,
  toggleVisibility,
}) => {
  const [isPublic, setIsPublic] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [data, setData] = useState<Directory[]>([]);
  const [directoryName, setDirectoryName] = useState("");

  const addDir = async (name: string, isPublic: boolean) => {
    try {
      await addNewDirectory(name, isPublic);
      setDirectoryName("");
      setIsPublic(false);
    } catch (e: any) {
      console.error(e);
    }
  };

  const loadDirectories = async () => {
    try {
      const directories = await fetchDirectories(1);
      setData(directories);
    } catch (error: any) {
      console.error("Error fetching data:", error);
      if (error.response) {
        console.error("Error status:", error.response.status);
        console.error("Error data:", error.response.data);
      }
    }
  };

  useEffect(() => {
    loadDirectories();
  }, []);

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
            Directory Name
          </label>
          <input
            name="name"
            className="border border-gray-300 p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Enter directory name"
            value={directoryName}
            onChange={(e) => setDirectoryName(e.target.value)}
          />
          <label
            htmlFor="public"
            className="flex items-center space-x-2 text-sm"
          >
            <input
              type="checkbox"
              name="public"
              className="form-checkbox text-blue-500 h-4 w-4"
              checked={isPublic}
              onChange={(e) => setIsPublic(e.target.checked)}
            />
            <span>Public?</span>
          </label>
          <button
            className="bg-blue-500 text-white rounded-md p-2 hover:bg-blue-600 transition duration-300"
            onClick={() => addDir(directoryName, isPublic)}
          >
            Add
          </button>
        </div>
      </Modal>

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
      <div className=" flex flex-row min-h-screen  mt-1 bg-gray-100 ">
        <div className="pl-2 basis-1/6 bg-slate-50 ">
          <button
            className="  bg-orange-400 border-grey-400 shadow-md hover:bg-orange-500 hover:shadow-sm border-2 p-4 rounded-full "
            onClick={() => setOpenModal(true)}
          >
            <div className="flex items-center justify-center">
              <FaPlus className="mr-1" style={{ color: "white" }} />
              <h3> Create Dircotry</h3>
            </div>
          </button>
          <hr className="mt-4"></hr>
        </div>
        <button onClick={() => deleteDirectory(1, Number(data[0].id))}>
          TEST
        </button>
        <div className=" basis-5/6 rounded-lg border-gray-200 border-2 bg-white p-1">
          <DirectoriesTable data={data} toggelVisibility={toggleVisibility} />
        </div>
      </div>
    </div>
  );
};

export default DirectoriesPage;
