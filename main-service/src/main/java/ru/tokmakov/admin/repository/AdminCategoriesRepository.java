package ru.tokmakov.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tokmakov.model.Category;


@Repository
public interface AdminCategoriesRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
