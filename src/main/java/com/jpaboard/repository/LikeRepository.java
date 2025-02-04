package com.jpaboard.repository;

import com.jpaboard.entity.VideoLikes;
import com.jpaboard.repository.custom.LikeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<VideoLikes, Long>, LikeRepositoryCustom {
    long countByVideo_VideoId(Long videoId);
}
