import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import logo from "../assets/logo.png";

function SignupPage() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");

  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  // ✅ Validation helper
  const validateForm = () => {
    const newErrors = {};

    if (!firstName.trim()) newErrors.firstName = "First name is required.";
    if (!lastName.trim()) newErrors.lastName = "Last name is required.";

    // Email pattern check
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!email) newErrors.email = "Email is required.";
    else if (!emailPattern.test(email))
      newErrors.email = "Please enter a valid email address.";

    // Password strength check
    const passwordPattern = /^(?=.*[A-Z])(?=.*\d).{6,}$/;
    if (!password) newErrors.password = "Password is required.";
    else if (!passwordPattern.test(password))
      newErrors.password =
        "Password must be at least 6 characters, include 1 uppercase letter and 1 number.";

    // Confirm password check
    if (!confirmPassword) newErrors.confirmPassword = "Please confirm password.";
    else if (password !== confirmPassword)
      newErrors.confirmPassword = "Passwords do not match.";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSignup = async (e) => {
    e.preventDefault();

    if (!validateForm()) return; // ❌ Stop if validation fails

    const user = { firstName, lastName, email, password };

    try {
      const response = await fetch("http://localhost:8080/api/auth/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user),
      });

      if (response.ok) {
        setMessage("Signup successful! Redirecting to login page...");
        setTimeout(() => navigate("/login"), 2500);
      } else {
        const data = await response.json();
        setMessage(data.message || "Signup failed. Please try again.");
      }
    } catch (error) {
      console.error("Signup error:", error);
      setMessage("Something went wrong. Please try again later.");
    }
  };

  return (
    <div
      className="d-flex flex-column justify-content-center align-items-center vh-100"
      style={{ backgroundColor: "#DEDED1" }}
    >
      {/* Logo at the top */}
      <div className="text-center mb-3">
        <img
          src={logo}
          alt="Ecom Logo"
          style={{ height: "70px", cursor: "pointer" }}
          onClick={() => navigate("/")}
        />
      </div>

      <div
        className="card p-4 shadow-lg"
        style={{
          width: "100%",
          maxWidth: "420px",
          borderRadius: "20px",
          backgroundColor: "#FBF3D1",
        }}
      >
        <h3 className="text-center mb-4" style={{ color: "#6B705C" }}>
          Create Account
        </h3>

        <form onSubmit={handleSignup} noValidate>
          <div className="row">
            <div className="col">
              <label className="form-label">First Name</label>
              <input
                type="text"
                className={`form-control ${
                  errors.firstName ? "is-invalid" : ""
                }`}
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
              />
              {errors.firstName && (
                <div className="invalid-feedback">{errors.firstName}</div>
              )}
            </div>

            <div className="col">
              <label className="form-label">Last Name</label>
              <input
                type="text"
                className={`form-control ${errors.lastName ? "is-invalid" : ""}`}
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
              />
              {errors.lastName && (
                <div className="invalid-feedback">{errors.lastName}</div>
              )}
            </div>
          </div>

          <div className="mt-3">
            <label className="form-label">Email</label>
            <input
              type="email"
              className={`form-control ${errors.email ? "is-invalid" : ""}`}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            {errors.email && (
              <div className="invalid-feedback">{errors.email}</div>
            )}
          </div>

          <div className="mt-3">
            <label className="form-label">Create Password</label>
            <input
              type="password"
              className={`form-control ${errors.password ? "is-invalid" : ""}`}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            {errors.password && (
              <div className="invalid-feedback">{errors.password}</div>
            )}
          </div>

          <div className="mt-3">
            <label className="form-label">Confirm Password</label>
            <input
              type="password"
              className={`form-control ${
                errors.confirmPassword ? "is-invalid" : ""
              }`}
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
            {errors.confirmPassword && (
              <div className="invalid-feedback">{errors.confirmPassword}</div>
            )}
          </div>

          <button
            type="submit"
            className="btn w-100 mt-4"
            style={{
              backgroundColor: "#B6AE9F",
              color: "white",
              fontWeight: "bold",
            }}
          >
            Sign Up
          </button>
        </form>

        {message && (
          <div
            className="alert alert-info text-center mt-3 py-2"
            style={{
              borderRadius: "10px",
              backgroundColor: "#C5C7BC",
              color: "#333",
            }}
          >
            {message}
          </div>
        )}

        <div className="text-center mt-3">
          <small>
            Already have an account?{" "}
            <Link to="/login" className="text-decoration-none">
              Login
            </Link>
          </small>
        </div>
      </div>
    </div>
  );
}

export default SignupPage;
