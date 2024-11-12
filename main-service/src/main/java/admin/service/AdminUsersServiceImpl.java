package admin.service;

import dto.user.NewUserRequest;
import dto.user.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUsersServiceImpl implements AdminUsersService {
    @Override
    public List<UserDto> findUsers(List<Long> ids, int from, int size) {
        return List.of();
    }

    @Override
    public UserDto saveUser(NewUserRequest newUser) {
        return null;
    }

    @Override
    public void deleteUser(long userId) {

    }
}
