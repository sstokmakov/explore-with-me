package ru.tokmakov.controller.guest;

import ru.tokmakov.dto.category.CategoryDto;
import ru.tokmakov.service.guest.GuestCategoriesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class GuestCategoriesController {
    private final GuestCategoriesService guestCategoriesService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> findCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("GET /categories - Parameters: from={}, size={}", from, size);

        List<CategoryDto> categories = guestCategoriesService.findCategories(from, size);

        log.info("GET /categories - Response: {} categories found", categories.size());
        return categories;
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto findCategoryById(@PathVariable int catId) {
        log.info("GET /categories/{} - Request received", catId);

        CategoryDto category = guestCategoriesService.findCategoryById(catId);

        log.info("GET /categories/{} - Response: {}", catId, category);
        return category;
    }
}
