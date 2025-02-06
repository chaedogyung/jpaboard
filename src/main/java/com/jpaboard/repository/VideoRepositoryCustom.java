package com.jpaboard.repository;

import com.jpaboard.dto.SearchDto;
import com.jpaboard.dto.VideoDetailDto;
import com.querydsl.core.Tuple;

import java.util.List;

public interface VideoRepositoryCustom {
    VideoDetailDto findVideoDetailWithLikeCount(Long videoId, String userId);

    List<Tuple> integratedSearch1(SearchDto search);
}
