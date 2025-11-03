import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import Navbar from "../component/Navbar";
import Pagination from "../component/Pagination";

function SearchResultsPage() {
  const [products, setProducts] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const keyword = queryParams.get("keyword");

  useEffect(() => {
    const fetchSearchResults = async () => {
      if (!keyword) return;

      try {
        setLoading(true);
        const response = await fetch(
          `http://localhost:8080/api/products/search?keyword=${keyword}&page=${currentPage}&size=5`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
          }
        );

        if (!response.ok) {
          console.error("Error fetching search results:", response.status);
          return;
        }

        const data = await response.json();
        console.log("Search results:", data);

        if (data.content) {
          setProducts(data.content);
          setTotalPages(data.totalPages);
        } else {
          setProducts(data);
          setTotalPages(1);
        }
      } catch (err) {
        console.error("Fetch failed:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchSearchResults();
  }, [keyword, currentPage]);
  const handlePageChange = (page) => {
    if (page >= 0 && page < totalPages) {
      setCurrentPage(page);
    }
  };

  return (
    <div style={{ backgroundColor: "#FBF3D1", minHeight: "100vh" }}>
      <Navbar />
      <div className="container py-5">
        <h3 className="text-center mb-4" style={{ color: "#1A2A4F" }}>
          Search Results for "{keyword}"
        </h3>

        {loading ? (
          <div className="text-center">
            <div className="spinner-border" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : (products.length > 0 && keyword) ? (
          <>
            <div className="row">
              {products.map((p) => (
                <div className="col-md-4 mb-4" key={p.id}>
                  <div
                    className="card shadow-sm h-100"
                    style={{
                      backgroundColor: "#DEDED1",
                      border: "none",
                    }}
                  >
                    <div className="card-body text-center">
                      <h5 style={{ color: "#1A2A4F" }}>{p.name}</h5>
                      <p style={{ marginBottom: "6px", color: "#6c757d" }}>
                        <strong>MRP:</strong>{" "}
                        <span style={{ textDecoration: "line-through" }}>
                          ₹{p.mrp}
                        </span>
                      </p>
                      <p style={{ marginBottom: "6px", color: "#1A2A4F" }}>
                        <strong>Discounted:</strong> ₹{p.discountedPrice}
                      </p>
                      <p style={{ color: "#555" }}>
                        <strong>Quantity:</strong> {p.qty}
                      </p>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* ✅ Pagination */}
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
          </>
        ) : (
          <p className="text-center" style={{ color: "#1A2A4F" }}>
            No products found.
          </p>
        )}
      </div>
    </div>
  );
}

export default SearchResultsPage;
