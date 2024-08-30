import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // proxy: "http://localhost:8080",
  server: {
    port: 3000,
  },
});
