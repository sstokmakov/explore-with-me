package ru.tokmakov.dto.category;

import lombok.experimental.UtilityClass;
import ru.tokmakov.model.Category;

@UtilityClass
public class CategoryMapper {
    public static Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static CategoryDto categoryToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
