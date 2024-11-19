package ru.tokmakov.controller.admin;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import ru.tokmakov.dto.user.NewUserRequest;
import ru.tokmakov.dto.user.UserDto;
import ru.tokmakov.service.admin.AdminUsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class AdminUsersController {
    private final AdminUsersService adminUsersService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findUsers(@RequestParam(required = false) Set<Long> ids,
                                   @RequestParam(required = false, defaultValue = "0") int from,
                                   @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("GET /admin/users - Fetching users with parameters: ids={}, from={}, size={}", ids, from, size);

        List<UserDto> users = adminUsersService.findUsers(ids, from, size);

        log.info("GET /admin/users - Found {} users", users.size());
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Validated @NotNull @RequestBody NewUserRequest newUser) {
        log.info("POST /admin/users - Creating new user with data: {}", newUser);

        UserDto savedUser = adminUsersService.saveUser(newUser);

        log.info("POST /admin/users - User created successfully: {}", savedUser);
        return savedUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        log.info("DELETE /admin/users/{} - Deleting user with userId={}", userId, userId);

        adminUsersService.deleteUser(userId);

        log.info("DELETE /admin/users/{} - User deleted successfully", userId);
    }
}