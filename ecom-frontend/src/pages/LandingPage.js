// src/pages/LandingPage.js
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./LandingPage.css"; // 👈 custom animations & styling
import logo from "../assets/logo.png"; // 👈 add your logo here (place logo.png inside src/assets/)

export default function LandingPage() {
  const navigate = useNavigate();

  useEffect(() => {
    // trigger animations on mount
    document.body.classList.add("page-loaded");
  }, []);

  return (
    <div className="landing-page">
      {/* Header */}

      {/* Hero Section */}
      <main className="landing-main text-center container">
        {/* 🖼️ Logo Section */}
        <div className="fade-slide logo-container">
          <img
            src={logo}
            alt="ShopEase Logo"
            className="landing-logo mb-4"
          />
        </div>

        {/* Main Headline */}
        <h1 className="fade-slide">
          Discover Shopping with <span className="highlight">Ease</span>
        </h1>

        <p className="fade-slide delay-1">
          Explore exclusive collections and enjoy a seamless shopping experience.
        </p>

        <div className="button-group fade-slide delay-2">
          <button className="btn login-btn" onClick={() => navigate("/login")}>
            Login
          </button>
          <button className="btn signup-btn" onClick={() => navigate("/signup")}>
            Sign Up
          </button>
        </div>
      </main>

      {/* Footer */}
      <footer className="landing-footer">
        © {new Date().getFullYear()} ShopEase. All Rights Reserved.
      </footer>
    </div>
  );
}
