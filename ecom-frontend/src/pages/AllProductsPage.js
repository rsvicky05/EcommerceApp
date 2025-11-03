import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../component/Navbar";

function AllProductsPage() {
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // ✅ Check if user is logged in (by verifying cookie with backend)
  const checkAuth = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/auth/validate", {
        method: "GET",
        credentials: "include", // send cookie
      });

      if (!response.ok) {
        // Token invalid or expired
        alert("Session expired or unauthorized. Please log in again.");
        navigate("/login");
        return false;
      }

      return true;
    } catch (error) {
      console.error("Error checking authentication:", error);
      alert("Please log in first.");
      navigate("/login");
      return false;
    }
  };

  // ✅ Fetch paginated products
  const fetchProducts = async (pageNum = 0) => {
    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/products/page?page=${pageNum}&size=12`,
        {
          method: "GET",
          headers: { "Content-Type": "application/json" },
          credentials: "include", // send JWT cookie
        }
      );

      if (!response.ok) {
        console.error("Failed to fetch products:", response.status);
        setLoading(false);
        return;
      }

      const data = await response.json();
      setProducts(data.content || []);
      setTotalPages(data.totalPages);
      setLoading(false);
    } catch (err) {
      console.error("Error fetching products:", err);
      setLoading(false);
    }
  };

  useEffect(() => {
    (async () => {
      const isAuthenticated = await checkAuth();
      if (isAuthenticated) {
        fetchProducts(page);
      }
    })();
  }, [page]);


  const handlePageChange = (pageNum) => {
    setPage(pageNum);
  };

  return (
    <div style={{ backgroundColor: "#FBF3D1", minHeight: "100vh" }}>
      <Navbar />

      <div className="container py-5">
        <div className="d-flex justify-content-center align-items-center mb-4">
          <h3 style={{ color: "#1A2A4F" }}>All Products</h3>
        </div>

        {loading ? (
          <p className="text-center text-muted">Loading products...</p>
        ) : products.length === 0 ? (
          <p className="text-center text-muted">No products found.</p>
        ) : (
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
        )}

        {/* Pagination */}
        <div className="d-flex justify-content-center align-items-center mt-4 flex-wrap">
          <nav aria-label="Product pagination">
            <ul className="pagination pagination-sm">
              <li className={`page-item ${page === 0 ? "disabled" : ""}`}>
                <button
                  className="page-link"
                  onClick={() => setPage(page - 1)}
                  style={{
                    backgroundColor: "#FBF3D1",
                    border: "1px solid #C5C7BC",
                    color: "#1A2A4F",
                  }}
                >
                  &laquo;
                </button>
              </li>

              {Array.from({ length: totalPages }, (_, i) => i).map((num) => {
                if (
                  num === 0 ||
                  num === totalPages - 1 ||
                  (num >= page - 1 && num <= page + 1)
                ) {
                  return (
                    <li
                      key={num}
                      className={`page-item ${num === page ? "active" : ""}`}
                    >
                      <button
                        className="page-link"
                        onClick={() => handlePageChange(num)}
                        style={{
                          backgroundColor:
                            num === page ? "#B6AE9F" : "#FBF3D1",
                          color: "#1A2A4F",
                          border: "1px solid #C5C7BC",
                        }}
                      >
                        {num + 1}
                      </button>
                    </li>
                  );
                } else if (
                  (num === page - 2 && num > 0) ||
                  (num === page + 2 && num < totalPages - 1)
                ) {
                  return (
                    <li key={num} className="page-item disabled">
                      <span
                        className="page-link"
                        style={{
                          backgroundColor: "#FBF3D1",
                          border: "none",
                          color: "#1A2A4F",
                        }}
                      >
                        ...
                      </span>
                    </li>
                  );
                } else {
                  return null;
                }
              })}

              <li
                className={`page-item ${page + 1 >= totalPages ? "disabled" : ""}`}
              >
                <button
                  className="page-link"
                  onClick={() => setPage(page + 1)}
                  style={{
                    backgroundColor: "#FBF3D1",
                    border: "1px solid #C5C7BC",
                    color: "#1A2A4F",
                  }}
                >
                  &raquo;
                </button>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  );
}

export default AllProductsPage;
