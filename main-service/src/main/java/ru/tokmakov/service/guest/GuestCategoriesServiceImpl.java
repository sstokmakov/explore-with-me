package ru.tokmakov.service.guest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.dto.category.CategoryDto;
import org.springframework.stereotype.Service;
import ru.tokmakov.dto.category.CategoryMapper;
import ru.tokmakov.exception.BadRequestException;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.model.Category;
import ru.tokmakov.repository.CategoryRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestCategoriesServiceImpl implements GuestCategoriesService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findCategories(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Invalid pagination parameters: from=" + from + ", size=" + size);
        }

        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);

        return categoryPage.stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findCategoryById(int catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));

        return CategoryMapper.toDto(category);
    }
}