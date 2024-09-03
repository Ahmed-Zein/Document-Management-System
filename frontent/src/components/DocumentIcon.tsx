import React from "react";
import {
  FaFile,
  FaFileCsv,
  FaFileExcel,
  FaFilePdf,
  FaFileWord,
  FaMarkdown,
} from "react-icons/fa";
import { FaImage } from "react-icons/fa6";
const className = "h-7 w-7";

const iconMap: { [key: string]: React.ReactNode } = {
  "text/markdown": <FaMarkdown className={className} />,
  "application/pdf": <FaFilePdf className={className} />,
  "application/msword": <FaFileWord className={className} />,
  "application/vnd.ms-excel": <FaFileExcel className={className} />,
  "text/csv": <FaFileCsv className={className} />,
  "image/jpeg": <FaImage className={className} />,
  default: <FaFile className={className} />,
};

interface DocumentIconProps {
  type: string;
  className?: string;
}

const DocumentIcon: React.FC<DocumentIconProps> = ({ type }) => {
  const IconComponent = iconMap[type] || iconMap["default"];

  return IconComponent;
};

export default DocumentIcon;
