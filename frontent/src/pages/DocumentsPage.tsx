import React, { useEffect, useState } from "react";
import { Document } from "../types/document";
interface DirectoryPageProps {
  fetchDocuments: (a: number, b: number) => Promise<Document[]>;
}
const DirectoryPage: React.FC<DirectoryPageProps> = ({ fetchDocuments }) => {
  const [documents, setDocuments] = useState<Document[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchDocumentsHandler = async () => {
    try {
      setLoading(true);
      const data = await fetchDocuments(1, 1);
      setDocuments(data);
    } catch (error: any) {
      console.error(error);
      setError(error.message || "Failed to fetch documents");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDocumentsHandler();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div>
      <h1>Directory Page</h1>
      {documents.length > 0 ? (
        documents.map((doc, index) => <li key={index}>{doc.name}</li>)
      ) : (
        <h1>No documents available</h1>
      )}
    </div>
  );
};

export default DirectoryPage;
