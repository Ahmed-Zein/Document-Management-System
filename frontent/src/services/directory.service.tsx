import axios from "axios";
import { Directory } from "../types/directory";

export const addNewDirectory = async (
  name: String,
  isPublic: boolean
): Promise<any> => {
  console.log("hi");
  const res = await axios.post(
    "http://localhost:8080/api/v1/users/1/workspace/directories",
    { name, isPublic },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  console.log(res);
  return {};
};

export const toggleVisibility = async (directory: Directory): Promise<any> => {
  try {
    const newVisibility = !directory.isPublic; // Toggle the current visibility

    const response = await axios.patch(
      `http://localhost:8080/api/v1/users/1/workspace/directories/${directory.id}`,
      {
        isPublic: newVisibility, // Set to the toggled value
      },
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
          "Content-Type": "application/json",
        },
      }
    );

    return response.data; // Return response data
  } catch (error) {
    console.error("Error toggling directory visibility:", error);
    throw error; // Re-throw error to handle it upstream if needed
  }
};
