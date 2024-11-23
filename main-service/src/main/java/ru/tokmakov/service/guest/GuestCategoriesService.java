package ru.tokmakov.service.guest;

import ru.tokmakov.dto.category.CategoryDto;

import java.util.List;

public interface GuestCategoriesService {
    List<CategoryDto> findCategories(int from, int size);

    CategoryDto findCategoryById(int catId);
}
