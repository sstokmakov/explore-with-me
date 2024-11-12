package ru.tokmakov.guest.controller;

import ru.tokmakov.dto.category.CategoryDto;
import ru.tokmakov.guest.service.GuestCategoriesService;
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
        return guestCategoriesService.findCategories(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto findCategoryById(@PathVariable int catId) {
        return guestCategoriesService.findCategoryById(catId);
    }
}
