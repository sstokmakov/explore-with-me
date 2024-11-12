package ru.tokmakov.admin.controller;

import ru.tokmakov.dto.user.NewUserRequest;
import ru.tokmakov.dto.user.UserDto;
import ru.tokmakov.admin.service.AdminUsersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class AdminUsersController {
    private final AdminUsersService adminUsersService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findUsers(@RequestParam List<Long> ids,
                                   @RequestParam(required = false, defaultValue = "0") int from,
                                   @RequestParam(required = false, defaultValue = "10") int size) {
        return adminUsersService.findUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Valid @NotNull @RequestBody NewUserRequest newUser) {
        return adminUsersService.saveUser(newUser);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam long userId) {
        adminUsersService.deleteUser(userId);
    }
}
