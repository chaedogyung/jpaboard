package com.jpaboard.repository;

import com.jpaboard.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Videos, Long>, VideoRepositoryCustom {
}
