import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Navbar from "../component/Navbar";

function CategoryProductPage() {
  const { categoryId } = useParams();
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(0);
  const [size] = useState(6); // items per page
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProductsByCategory = async () => {
      setLoading(true);
      try {
        const response = await fetch(
          `http://localhost:8080/api/products/category/${categoryId}?page=${page}&size=${size}`,
          {
            method: "GET",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
          }
        );

        if (!response.ok) {
          console.error("Error fetching products:", response.status);
          return;
        }

        const data = await response.json();
        setProducts(data.content || []); // Page<T> → data.content
        setTotalPages(data.totalPages || 0);
      } catch (err) {
        console.error("Fetch failed:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchProductsByCategory();
  }, [categoryId, page, size]);

  const handlePrev = () => setPage((prev) => Math.max(prev - 1, 0));
  const handleNext = () => setPage((prev) => Math.min(prev + 1, totalPages - 1));
  const handlePageClick = (pageNum) => setPage(pageNum);

  if (loading) return <div className="text-center py-5">Loading...</div>;

  return (
    <div>
      <Navbar />
      <div className="container py-5">
        <h3 className="text-center mb-4" style={{ color: "#1A2A4F" }}>
          Products in this Category
        </h3>

        {products.length === 0 ? (
          <p className="text-center text-muted">No products found.</p>
        ) : (
          <>
            <div className="row">
              {products.map((prod) => (
                <div key={prod.id} className="col-md-4 mb-4">
                  <div
                    className="card shadow-sm h-100 text-center"
                    style={{
                      backgroundColor: "#DEDED1",
                      border: "none",
                    }}
                  >
                    <div className="card-body">
                      <h5 style={{ color: "#1A2A4F" }}>{prod.name}</h5>
                      <p className="text-muted">MRP: ₹{prod.mrp}</p>
                      <p style={{ color: "#6c757d" }}>
                        Discounted: ₹{prod.discountedPrice}
                      </p>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Pagination Controls */}
            <div className="d-flex justify-content-center align-items-center mt-4">
              <button
                className="btn btn-outline-secondary mx-1"
                onClick={handlePrev}
                disabled={page === 0}
              >
                Previous
              </button>

              {[...Array(totalPages)].map((_, i) => (
                <button
                  key={i}
                  className={`btn mx-1 ${
                    i === page ? "btn-dark" : "btn-outline-secondary"
                  }`}
                  onClick={() => handlePageClick(i)}
                >
                  {i + 1}
                </button>
              ))}

              <button
                className="btn btn-outline-secondary mx-1"
                onClick={handleNext}
                disabled={page === totalPages - 1}
              >
                Next
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default CategoryProductPage;
