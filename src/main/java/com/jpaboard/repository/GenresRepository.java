package com.jpaboard.repository;

import com.jpaboard.entity.Genres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenresRepository  extends JpaRepository<Genres, Long>{
}
