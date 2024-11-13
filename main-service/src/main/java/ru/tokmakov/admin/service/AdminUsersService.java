package ru.tokmakov.admin.service;

import ru.tokmakov.dto.user.NewUserRequest;
import ru.tokmakov.dto.user.UserDto;

import java.util.List;
import java.util.Set;

public interface AdminUsersService {
    List<UserDto> findUsers(Set<Long> ids, int from, int size);

    UserDto saveUser(NewUserRequest newUser);

    void deleteUser(long userId);
}
