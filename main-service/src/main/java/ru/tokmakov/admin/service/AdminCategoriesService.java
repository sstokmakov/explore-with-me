package ru.tokmakov.admin.service;

import ru.tokmakov.dto.category.CategoryDto;
import ru.tokmakov.dto.category.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto saveCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(long catId, NewCategoryDto newCategoryDto);
}
