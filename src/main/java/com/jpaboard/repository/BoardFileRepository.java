package com.jpaboard.repository;

import com.jpaboard.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findByBno(Long bno);

    Optional<BoardFile> findById(Long fileNo);
}
