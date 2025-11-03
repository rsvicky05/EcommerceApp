import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Category() {
  const [categories, setCategories] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const navigate = useNavigate();

  // 🧠 Fetch paginated categories whenever page changes
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/categories/page?page=${currentPage}&size=6`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            credentials: "include", // ✅ Send JWT cookie
          }
        );

        if (!response.ok) {
          console.error("Error fetching categories:", response.status);
          return;
        }

        const data = await response.json();
        console.log("Fetch categories response:", data);

        setCategories(data.content || []);
        setTotalPages(data.totalPages || 0);
      } catch (err) {
        console.error("Fetch failed:", err);
      }
    };

    fetchCategories();
  }, [currentPage]);

  // 🧩 Pagination button click
  const handlePageChange = (page) => {
    if (page >= 0 && page < totalPages) {
      setCurrentPage(page);
    }
  };

  return (
    <div className="container py-5">
      <h3 className="text-center mb-4" style={{ color: "#1A2A4F" }}>
        Explore Categories
      </h3>

      {/* 🗂 Category Cards */}
      <div className="row">
        {categories.map((cat) => (
          <div key={cat.categoryId} className="col-md-4 mb-4">
            <div
              className="card shadow-sm h-100 text-center"
              style={{
                backgroundColor: "#DEDED1",
                cursor: "pointer",
                border: "none",
              }}
              onClick={() => navigate(`/category/${cat.categoryId}`)}
            >
              <div className="card-body">
                <h5 style={{ color: "#1A2A4F" }}>{cat.name}</h5>
                <p style={{ color: "#6c757d" }}>
                  {cat.description || "Browse products in this category"}
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* 🧭 Pagination Controls */}
      {totalPages > 1 && (
        <div className="d-flex justify-content-center mt-4">
          <button
            className="btn btn-outline-dark mx-1"
            disabled={currentPage === 0}
            onClick={() => handlePageChange(currentPage - 1)}
          >
            Prev
          </button>

          {/* Render numbered page buttons */}
          {[...Array(totalPages).keys()].map((page) => (
            <button
              key={page}
              className={`btn mx-1 ${
                currentPage === page
                  ? "btn-dark"
                  : "btn-outline-dark"
              }`}
              onClick={() => handlePageChange(page)}
            >
              {page + 1}
            </button>
          ))}

          <button
            className="btn btn-outline-dark mx-1"
            disabled={currentPage === totalPages - 1}
            onClick={() => handlePageChange(currentPage + 1)}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}

export default Category;
