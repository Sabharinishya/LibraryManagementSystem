package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.CategoryRequest;
import com.ey.dto.response.ApiResponse;
import com.ey.entity.Category;
import com.ey.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryRequest request) {
		if (categoryService.existsByName(request.getName())) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Category already exists"));
		}
		Category category = new Category();
		category.setName(request.getName());
		return ResponseEntity.ok(new ApiResponse<>(true, categoryService.create(category)));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
		return ResponseEntity.ok(new ApiResponse<>(true, categoryService.getAll()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
		categoryService.delete(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Category deleted successfully"));
	}
}