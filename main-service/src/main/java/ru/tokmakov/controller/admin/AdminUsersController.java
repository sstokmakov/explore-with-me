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
        log.info("Received request to find users with parameters - IDs: {}, from: {}, size: {}", ids, from, size);

        List<UserDto> users = adminUsersService.findUsers(ids, from, size);

        log.info("Retrieved {} users", users.size());
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Validated @NotNull @RequestBody NewUserRequest newUser) {
        log.info("Received request to save new user with email: {}", newUser.getEmail());

        UserDto savedUser = adminUsersService.saveUser(newUser);

        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {
        log.info("Received request to delete user with ID: {}", userId);

        adminUsersService.deleteUser(userId);
        log.info("Successfully deleted user with ID: {}", userId);
    }
}