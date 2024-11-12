package admin.service;

import dto.category.CategoryDto;
import dto.category.NewCategoryDto;
import org.springframework.stereotype.Service;

@Service
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        return null;
    }

    @Override
    public void deleteCategory(long catId) {

    }

    @Override
    public CategoryDto updateCategory(long catId, NewCategoryDto newCategoryDto) {
        return null;
    }
}
