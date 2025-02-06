package com.jpaboard.repository;

import com.jpaboard.dto.SearchDto;

import java.util.Map;

public interface MainRepositoryCustom {
    Map<String, Object> integratedSearch(SearchDto search);
}
