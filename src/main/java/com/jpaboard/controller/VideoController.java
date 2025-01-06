package com.jpaboard.controller;

import com.jpaboard.entity.Videos;
import com.jpaboard.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping(value = "/video/newReg")
    public String videoForm() {
        return "video/videoForm";
    }

    @PostMapping(value = "/video/new")
    public String uploadVideo(
            @RequestParam("videoFile") MultipartFile videoFile,
            @RequestParam("subtitles") MultipartFile subtitle,
            @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("genre") String genre,
            @RequestParam("release_date") String release_date,
            @RequestParam("duration") Integer duration,
            @RequestParam("language") String language,
            @RequestParam("age_rating") String age_rating,
            Model model) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // 출시일 변환
            Date releaseDate = dateFormat.parse(release_date);

            // VideoService를 통해 비디오 저장
            Videos video = videoService.saveVideo(videoFile, subtitle, thumbnailFile, title, description, genre, releaseDate, duration, language, age_rating);

            // 성공 메시지와 저장된 비디오 정보를 모델에 추가
            model.addAttribute("video", video);
            model.addAttribute("message", "동영상 업로드 성공!");
        } catch (IOException e) {
            // 파일 저장 오류 처리
            model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다.");
        } catch (ParseException e) {
            // 날짜 변환 오류 처리
            model.addAttribute("error", "출시일 형식이 올바르지 않습니다.");
        } catch (Exception e) {
            // 기타 예외 처리
            model.addAttribute("error", "알 수 없는 오류가 발생했습니다.");
        }

        // 업로드 결과 페이지로 이동
        return "redirect:/";
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
        System.out.println(fileName);

        String subtitlePath = video.getSubtitle_file_path();

        // 조회된 비디오 정보를 뷰에 전달
        ModelAndView mv = new ModelAndView("video/videoDetail");

        mv.addObject("video", video);
        mv.addObject("encodeUrl", fileName);
        mv.addObject("subtitlePath", subtitlePath);
        return mv; // 반환할 뷰의 이름
    }

    //동영상 목록
    @GetMapping("/video/manage")
    public String getVideoManageList(Model model) {
        model.addAttribute("videos", videoService.getAllVideos());
        return "video/videoManageList"; // 반환할 뷰의 이름
    }
}
