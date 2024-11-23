package ru.tokmakov.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tokmakov.model.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Boolean existsByTitle(String name);

    Page<Compilation> findByPinned(boolean pinned, Pageable pageable);
}
