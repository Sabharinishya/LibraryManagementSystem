package com.ey.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.entity.Category;
import com.ey.repository.CategoryRepository;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category create(Category category) {
		return categoryRepository.save(category);
	}

	public List<Category> getAll() {
		return categoryRepository.findAll();
	}

	public Category getById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
	}

	public void delete(Long id) {
		categoryRepository.deleteById(id);
	}

	public boolean existsByName(String name) {
		return categoryRepository.existsByName(name);
	}
}