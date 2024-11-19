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
        log.info("POST /admin/categories - Creating new category. Request data: {}", newCategoryDto);

        CategoryDto savedCategory = adminCategoriesService.saveCategory(newCategoryDto);

        log.info("POST /admin/categories - Category created successfully. Saved category: {}", savedCategory);
        return savedCategory;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.info("DELETE /admin/categories/{} - Deleting category with ID: {}", catId, catId);

        adminCategoriesService.deleteCategory(catId);

        log.info("DELETE /admin/categories/{} - Category deleted successfully", catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Validated @NotNull @RequestBody NewCategoryDto newCategoryDto) {
        log.info("PATCH /admin/categories/{} - Updating category with ID: {}. Request data: {}", catId, catId, newCategoryDto);

        CategoryDto updatedCategory = adminCategoriesService.updateCategory(catId, newCategoryDto);

        log.info("PATCH /admin/categories/{} - Category updated successfully. Updated category: {}", catId, updatedCategory);
        return updatedCategory;
    }
}