package com.jpaboard.service;

import com.jpaboard.dto.SearchDto;
import com.jpaboard.repository.MainRepository;
import com.jpaboard.repository.VideoRepository;
import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {

    private final MainRepository mainRepository;
    private final VideoRepository videoRepository;

    public Map<String, Object> integratedSearch(SearchDto search) {
        Map<String, Object> map = new HashMap<>();
        map = mainRepository.integratedSearch(search);
        return map;
    }

    public List<Tuple> integratedSearch1(SearchDto search) {
       List<Tuple> tuples = new ArrayList<>();
        tuples = videoRepository.integratedSearch1(search);
        return tuples;
    }
}
