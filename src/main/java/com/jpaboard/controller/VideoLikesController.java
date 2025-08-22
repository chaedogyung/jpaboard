package com.jpaboard.controller;

import com.jpaboard.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/like")
@RequiredArgsConstructor
public class VideoLikesController {

    private final VideoService videoService;

    //좋아요 동영상 목록
    @GetMapping("/video/favoriteVideo")
    public String getfavoriteVideoList(Model model) {

        videoService.getVideoLikeList();
        return "sidePage/videoFavorite"; // 반환할 뷰의 이름
    }

    //동영상 시청이력
    @GetMapping("/video/history")
    public String getVideoHistory(Model model) {
        return "sidePage/videoHistory"; // 반환할 뷰의 이름
    }

    //고객센터
    @GetMapping("/customer/center")
    public String getVideoContact(Model model) {
        return "sidePage/inquryVideo"; // 반환할 뷰의 이름
    }
}
