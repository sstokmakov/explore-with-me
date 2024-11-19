package ru.tokmakov.service.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.exception.ConflictException;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.repository.UserRepository;
import ru.tokmakov.dto.user.NewUserRequest;
import ru.tokmakov.dto.user.UserDto;
import org.springframework.stereotype.Service;
import ru.tokmakov.dto.user.UserMapper;
import ru.tokmakov.model.User;

import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class AdminUsersServiceImpl implements AdminUsersService {
    private final UserRepository adminUsersRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findUsers(Set<Long> ids, int from, int size) {
        log.info("Finding users with parameters: ids = {}, from = {}, size = {}", ids, from, size);

        List<User> result;
        if (ids == null) {
            log.info("Fetching all users from {} with a size of {}", from, size);
            result = adminUsersRepository.findAllUsers(from, size);
        } else {
            log.info("Fetching users with ids: {} from {} with a size of {}", ids, from, size);
            result = adminUsersRepository.findUsersByIds(ids, from, size);
        }

        log.info("Found {} users", result.size());

        return result.stream()
                .map(UserMapper::userToUserDto)
                .toList();
    }

    @Transactional
    @Override
    public UserDto saveUser(NewUserRequest newUser) {
        log.info("Starting saveUser method for new user with email: {}", newUser.getEmail());
        if (adminUsersRepository.existsByEmail(newUser.getEmail())) {
            log.warn("Email already exists: {}", newUser.getEmail());
            throw new ConflictException("Email already exists");
        }
        User user = UserMapper.newUserRequestToUser(newUser);
        User savedUser = adminUsersRepository.save(user);

        log.info("User with email {} successfully saved with ID: {}", savedUser.getEmail(), savedUser.getId());

        return UserMapper.userToUserDto(savedUser);
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        log.info("Attempting to delete user with ID: {}", userId);

        if (adminUsersRepository.existsById(userId)) {
            log.info("User with ID: {} exists. Proceeding to delete.", userId);
            adminUsersRepository.deleteById(userId);

            log.info("Successfully deleted user with ID: {}", userId);
        } else {
            log.warn("User with ID: {} not found. Deletion aborted.", userId);

            throw new NotFoundException("User with id " + userId + " not found");
        }
    }
}