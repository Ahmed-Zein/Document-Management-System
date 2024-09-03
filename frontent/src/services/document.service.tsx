import axios from "axios";
import { IDocument } from "../types/document";
const BASE_URL = "http://localhost:8080";

export const fetchDocuments = async (
  userId: Number,
  dirId: Number
): Promise<IDocument[]> => {
  const response = await axios.get(
    `${BASE_URL}/api/v1/users/${userId}/workspace/directories/${dirId}/documents`,
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
  return response.data as IDocument[];
};

export const uploadDocument = async (
  userId: Number,
  dirId: Number,
  file: File
) => {
  const formData = new FormData();
  formData.append("file", file);
  const res = await axios.post(
    `http://localhost:8080/api/v1/users/${userId}/workspace/directories/${dirId}/documents`,
    formData,
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
        "Content-Type": "multipart/form-data",
      },
    }
  );
  return res;
};

export const deleteDocument = async (
  userId: Number,
  dirId: Number,
  docId: string
) => {
  return await axios.delete(
    `${BASE_URL}/api/v1/users/${userId}/workspace/directories/${dirId}/documents/${docId}`,
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    }
  );
};
