import React from "react";

type props = { color: String };
const Dot: React.FC<props> = ({ color }) => {
  const dotStyle: object = {
    width: "20px",
    height: "20px",
    backgroundColor: color,
    borderRadius: "50%",
    display: "inline-block",
  };

  return <div style={dotStyle}></div>;
};

export default Dot;
