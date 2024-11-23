package ru.tokmakov.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.dto.category.CategoryDto;
import ru.tokmakov.dto.category.CategoryMapper;
import ru.tokmakov.dto.category.NewCategoryDto;
import org.springframework.stereotype.Service;
import ru.tokmakov.exception.CategoryNotEmptyException;
import ru.tokmakov.exception.ConflictException;
import ru.tokmakov.model.Category;
import ru.tokmakov.repository.CategoryRepository;
import ru.tokmakov.repository.EventRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        log.info("Attempting to save new category: {}", newCategoryDto);

        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            log.error("Category with name {} already exists", newCategoryDto.getName());
            throw new ConflictException("Category with name " + newCategoryDto.getName() + " already exists");
        }

        Category category = CategoryMapper.newCategoryDtoToCategory(newCategoryDto);

        Category savedCategory = categoryRepository.save(category);

        log.info("Category saved successfully: {}", savedCategory);

        return CategoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.info("Attempting to delete category with ID: {}", catId);

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> {
                    log.error("Category with ID {} not found. Deletion failed.", catId);
                    return new NotFoundException("Category with ID " + catId + " not found");
                });

        if (eventRepository.existsByCategoryId(category.getId())) {
            throw new CategoryNotEmptyException("Cannot delete category with ID " + catId + " category is not empty");
        } else {
            categoryRepository.delete(category);
            log.info("Category with ID {} successfully deleted.", catId);
        }
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        log.info("Starting update for category with ID: {}. New data: {}", catId, newCategoryDto);

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> {
                    log.error("Category with ID {} not found. Update failed.", catId);
                    return new NotFoundException("Category with ID " + catId + " not found");
                });

        if (categoryRepository.existsByName(newCategoryDto.getName())
            && !category.getName().equals(newCategoryDto.getName())) {
            log.warn("Category name '{}' already exists. Update for ID {} cannot proceed.", newCategoryDto.getName(), catId);
            throw new ConflictException("Category with name '" + newCategoryDto.getName() + "' already exists");
        }

        category.setName(newCategoryDto.getName());
        log.info("Updating category name to '{}'", newCategoryDto.getName());

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category with ID {} successfully updated.", catId);

        CategoryDto updatedCategoryDto = CategoryMapper.toDto(updatedCategory);
        log.info("Returning updated category DTO: {}", updatedCategoryDto);

        return updatedCategoryDto;
    }
}