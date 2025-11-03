import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Category from "../component/Category";
import Navbar from "../component/Navbar";

function HomePage() {
  const [categories, setCategories] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const navigate = useNavigate();

  // 🔒 Validate JWT when user enters Home page
  useEffect(() => {
    const validateToken = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/auth/validate", {
          method: "GET",
          credentials: "include", // send cookies to backend
        });

        if (!response.ok) {
          // ❌ Invalid or missing token — show alert and redirect
          alert("Please login first");
          navigate("/login");
          return;
        }

        // ✅ Token is valid — fetch categories
        fetchCategories();
      } catch (error) {
        console.error("Token validation failed:", error);
        alert("Please login first");
        navigate("/login");
      }
    };

    validateToken();
  }, [navigate]);

  // 🛍️ Load categories after token validation success
  const fetchCategories = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/categories?page=${page}&size=6`, {
        credentials: "include",
      });
      if (!res.ok) throw new Error("Failed to load categories");
      const data = await res.json();
      setCategories(data.content || []);
      setTotalPages(data.totalPages || 0);
    } catch (err) {
      console.error("Error loading categories:", err);
    }
  };

  return (
    <div style={{ backgroundColor: "#FBF3D1", minHeight: "100vh" }}>
      <Navbar />
      <Category />
    </div>
  );
}

export default HomePage;
