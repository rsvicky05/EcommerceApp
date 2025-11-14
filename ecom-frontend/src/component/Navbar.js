import React from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/logo.png"
function Navbar() {
  const navigate = useNavigate();

  function handleLogout() {
  fetch("http://localhost:8080/api/auth/logout", {
    method: "POST",
    credentials: "include", // so cookie is sent
  })
    .then((res) => {
      if (res.ok) {
        alert("Logged out successfully");
        navigate("/", { replace: true });
        //window.location.href = "/"; // redirect to login
      } else {
        alert("Logout failed");
      }
    })
    .catch((err) => {
      console.error("Logout error:", err);
      alert("Logout failed");
    });
}


  const handleAllProducts = () => {
    navigate("/products");
  };

  const handleHome = () => {
    navigate("/home");
  };
  const [searchTerm, setSearchTerm] = React.useState("");
  const handleSearch = (e) => {
    e.preventDefault();
    navigate(`/search?keyword=${searchTerm}`);
  };

  return (
    <nav
        className="navbar navbar-expand-lg shadow-sm"
        style={{ backgroundColor: "#DEDED1" }}
      >
        <div className="container-fluid">
          <div
            className="navbar-brand d-flex align-items-center"
            style={{ cursor: "pointer" }}
            onClick={() => navigate("/home")}
          >
            <img
              src={logo}
              alt="ShopEase"
              width="40"
              height="40"
              className="me-2"
            />
            <h5 className="m-0" style={{ color: "#1A2A4F" }}>
              ShopEase
            </h5>
          </div>

          <form
            className="d-flex mx-auto"
            role="search"
            onSubmit={handleSearch}
            style={{ width: "40%" }}
          >
            <input
              className="form-control me-2"
              type="search"
              placeholder="Search products..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              style={{
                backgroundColor: "#C5C7BC",
                color: "#1A2A4F",
                border: "none",
              }}
            />
            <button className="btn btn-outline-dark" type="submit">
              Search
            </button>
          </form>

          <div>
            <button
              className="btn me-2"
              style={{ backgroundColor: "#B6AE9F", color: "#1A2A4F" }}
              onClick={() => navigate("/products")}
            >
              All Products
            </button>
            <button
              className="btn"
              style={{ backgroundColor: "#C5C7BC", color: "#1A2A4F" }}
              onClick={handleLogout}
            >
              Logout
            </button>
          </div>
        </div>
      </nav>
  );
}

export default Navbar;
