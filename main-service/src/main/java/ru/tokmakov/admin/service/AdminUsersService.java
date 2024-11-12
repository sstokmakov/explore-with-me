package ru.tokmakov.admin.service;

import ru.tokmakov.dto.user.NewUserRequest;
import ru.tokmakov.dto.user.UserDto;

import java.util.List;

public interface AdminUsersService {
    List<UserDto> findUsers(List<Long> ids, int from, int size);

    UserDto saveUser(NewUserRequest newUser);

    void deleteUser(long userId);
}
