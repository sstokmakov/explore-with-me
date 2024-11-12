package admin.service;

import dto.category.CategoryDto;
import dto.category.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto saveCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(long catId, NewCategoryDto newCategoryDto);
}
