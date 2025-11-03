// src/api/api.js
const API_BASE_URL = "http://localhost:8080/api";

export const api = {
  // --- Authentication ---
  signup: async (firstName, lastName, email, password) => {
    const res = await fetch(`${API_BASE_URL}/auth/signup`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ firstName, lastName, email, password }),
      credentials: "include",
    });
    return res;
  },

  login: async (email, password) => {
    const res = await fetch(`${API_BASE_URL}/auth/signin`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
      credentials: "include",
    });
    return res;
  },

  // --- Categories ---
  getCategories: async (page = 0, size = 6) => {
    const res = await fetch(`${API_BASE_URL}/categories?page=${page}&size=${size}`, {
      method: "GET",
      credentials: "include",
    });
    return res;
  },

  // --- Products ---
  getProductsByCategory: async (categoryId, page = 0, size = 6) => {
    const res = await fetch(`${API_BASE_URL}/products/category/${categoryId}?page=${page}&size=${size}`, {
      method: "GET",
      credentials: "include",
    });
    return res;
  },

  getAllProducts: async (page = 0, size = 6) => {
    const res = await fetch(`${API_BASE_URL}/products/page?page=${page}&size=${size}`, {
      method: "GET",
      credentials: "include",
    });
    return res;
  },

  searchProducts: async (keyword) => {
    const res = await fetch(`${API_BASE_URL}/products/search?keyword=${keyword}`, {
      method: "GET",
      credentials: "include",
    });
    return res;
  },
};
