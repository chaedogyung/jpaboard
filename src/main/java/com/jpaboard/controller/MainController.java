package com.jpaboard.controller;

import com.jpaboard.entity.Videos;
import com.jpaboard.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final VideoService videoService;

    @GetMapping(value = "/")
    public String main() {
//        ModelAndView mv = new ModelAndView("main");
        return "main";
    }

    @GetMapping("/test")
    public String test() {
        return "CORS 설정이 제대로 동작합니다!";
    }

    //동영상 목록
    @GetMapping("/video/list")
    public ModelAndView getVideoList(/*Model model*/) {
        ModelAndView mv = new ModelAndView("video/videoList");
        mv.addObject("videos", videoService.getAllVideos());
        return mv; // 반환할 뷰의 이름
    }

    // 동영상 상세 페이지
    @GetMapping("/video/{video_id}")
    public ModelAndView getVideoDetail(@PathVariable Long video_id) throws UnsupportedEncodingException {
        // 동영상 ID로 정보를 조회
        Videos video = videoService.getVideoById(video_id);
        String filePath = video.getVideo_url();
        int lastIndexOfBackslash = filePath.lastIndexOf("\\");
        String fileName = filePath.substring(lastIndexOfBackslash + 1);
        System.out.println(fileName); // 출력: [영광패밀리] 드래곤볼Z 122.mp4

        String subtitlePath = video.getSubtitle_file_path();

        // 조회된 비디오 정보를 뷰에 전달
        ModelAndView mv = new ModelAndView("video/videoDetail");

        mv.addObject("video", video);
        mv.addObject("encodeUrl", fileName);
        mv.addObject("subtitlePath", subtitlePath);
        return mv; // 반환할 뷰의 이름
    }

}
