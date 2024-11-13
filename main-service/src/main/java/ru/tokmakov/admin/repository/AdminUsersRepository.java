package ru.tokmakov.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tokmakov.model.User;

import java.util.List;
import java.util.Set;

@Repository
public interface AdminUsersRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query(value = "SELECT u.id, u.email, u.name FROM users u ORDER BY u.id OFFSET :from LIMIT :size", nativeQuery = true)
    List<User> findAllUsers(int from, int size);

    @Query(value = "SELECT u.id, u.email, u.name FROM users u WHERE u.id IN :ids ORDER BY u.id OFFSET :from LIMIT :size", nativeQuery = true)
    List<User> findUsersByIds(Set<Long> ids, int from, int size);
}