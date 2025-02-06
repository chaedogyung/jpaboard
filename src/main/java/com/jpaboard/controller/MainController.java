package com.jpaboard.controller;

import com.jpaboard.dto.SearchDto;
import com.jpaboard.dto.VideoDTO;
import com.jpaboard.entity.BoardVO;
import com.jpaboard.entity.Videos;
import com.jpaboard.service.MainService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping(value = "/")
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView("main");
        return modelAndView;
    }

    //메인 검색
    @GetMapping(value = "/integratedSearch")
    public ModelAndView MainSearch(SearchDto search) {
        ModelAndView model = new ModelAndView("integratedSearch");
        Map<String, Object> modelMap = new HashMap<>();
        List<Tuple> results = new ArrayList<>();

        modelMap = mainService.integratedSearch(search);
        results = mainService.integratedSearch1(search);

        Object obj = modelMap.get("board");
        List<BoardVO> boardVOList = new ArrayList<>();

        if (obj instanceof List<?>) {
            for (Object item : (List<?>) obj) {
                if (item instanceof BoardVO) {
                    boardVOList.add((BoardVO) item);
                }
            }
        }

        // 동영상 검색 결과 (Tuple → DTO 변환)
        List<VideoDTO> videoDTOList = results.stream()
                .map(tuple -> new VideoDTO(
                        tuple.get(0, Long.class),  // videoId
                        tuple.get(1, String.class),  // title
                        tuple.get(2, String.class)   // description
                ))
                .toList();
        model.addObject("integratedSearch", boardVOList);
        model.addObject("integratedSearchVideos", videoDTOList);
        return model;
    }
}
