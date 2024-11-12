package ru.tokmakov.guest.service;

import ru.tokmakov.dto.category.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestCategoriesServiceImpl implements GuestCategoriesService {
    @Override
    public List<CategoryDto> findCategories(int from, int size) {
        return List.of();
    }

    @Override
    public CategoryDto findCategoryById(int catId) {
        return null;
    }
}
