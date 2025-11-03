package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public String addCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            return "Category already exists!";
        }
        categoryRepository.save(category);
        return "Category added successfully!";
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Page<Category> getPaginatedCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable);
    }

    public String deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            return "Category not found!";
        }
        categoryRepository.deleteById(id);
        return "Category deleted successfully!";
    }
}
