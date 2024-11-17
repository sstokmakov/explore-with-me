package ru.tokmakov.controller.admin;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.tokmakov.dto.category.CategoryDto;
import ru.tokmakov.dto.category.NewCategoryDto;
import ru.tokmakov.service.admin.AdminCategoriesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoriesController {
    private final AdminCategoriesService adminCategoriesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@Validated @NotNull @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Request received to save category: {}", newCategoryDto);

        CategoryDto savedCategory = adminCategoriesService.saveCategory(newCategoryDto);

        log.info("Category saved successfully: {}", savedCategory);

        return savedCategory;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.info("Attempting to delete category with ID: {}", catId);
        adminCategoriesService.deleteCategory(catId);
        log.info("Category deleted successfully: {}", catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable long catId,
                                      @Validated @NotNull @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Attempting to update category with ID: {}", catId);
        CategoryDto updatedCategory = adminCategoriesService.updateCategory(catId, newCategoryDto);
        log.info("Category updated successfully: {}", catId);
        return updatedCategory;
    }
}