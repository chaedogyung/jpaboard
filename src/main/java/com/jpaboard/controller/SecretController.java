package com.jpaboard.controller;

import com.jpaboard.entity.Secret_videos;
import com.jpaboard.service.SecretVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/secret/*")
@RequiredArgsConstructor
public class SecretController {

    private final SecretVideoService secretVideoService;

    private static final long MAX_VIDEO_SIZE = 5_500L * 1024 * 1024; // 동영상 최대 5.5GB
    private static final long MAX_OTHER_FILE_SIZE = 20L * 1024 * 1024; // 썸네일/자막 최대 20MB


    @GetMapping(value = "/video/newReg")
    public String videoForm() {
        return "secret/secretVideoForm";
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
            Model model) throws ParseException, IOException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 출시일 변환
        Date releaseDate = dateFormat.parse(release_date);

        // 자막 파일 크기 및 확장자 검증
        String sub = validateSubtitleFile(subtitle);
        if (!sub.equals("")) {
            model.addAttribute("error", sub);
            return "video/videoForm";
        }

        // 동영상 파일 크기 및 확장자 검증
        String videoString = validateVideoFile(videoFile);
        if (!videoString.equals("")) {
            model.addAttribute("error", videoString);
            return "video/videoForm";
        }

        // 썸네일 파일 크기 및 확장자 검증
        String thumbnailString = validateImageFile(thumbnailFile);
        if (!thumbnailString.equals("")) {
            model.addAttribute("error", thumbnailString);
            return "video/videoForm";
        }

        // secretVideoService를 통해 비디오 저장
        Secret_videos secret_videos = secretVideoService.saveVideo(videoFile, subtitle, thumbnailFile, title, description, genre, releaseDate, duration, language, age_rating);

        // 성공 메시지와 저장된 비디오 정보를 모델에 추가
        model.addAttribute("video", secret_videos);

        // 업로드 결과 페이지로 이동
        return "redirect:/";
    }

    // 자막 파일 검증
    private String validateSubtitleFile(MultipartFile subtitle) {
        String returnString = "";
        if (subtitle.getSize() > MAX_OTHER_FILE_SIZE) {
            return "자막 파일 크기는 최대 20MB까지만 허용됩니다.";
        }

        String extension = getFileExtension(subtitle.getOriginalFilename());
        if (!extension.equals("")) {
            if (!"vtt".equalsIgnoreCase(extension)) {
                return "자막 파일은 .vtt 형식만 허용됩니다.";
            }
        }

        return returnString;
    }

    // 동영상 파일 검증
    private String validateVideoFile(MultipartFile videoFile) {
        String returnString = "";
        if (videoFile.getSize() > MAX_VIDEO_SIZE) {
            returnString = "동영상 파일 크기는 최대 5.5GB까지만 허용됩니다.";
        }
        String extension = getFileExtension(videoFile.getOriginalFilename());
        if (!extension.matches("mp4|mkv|avi|mov|wmv")) {
            returnString = "허용되지 않은 동영상 파일 형식입니다.";
        }
        return returnString;
    }

    // 썸네일 이미지 파일 검증
    private String validateImageFile(MultipartFile imageFile) {
        String returnString = "";
        if (imageFile.getSize() > MAX_OTHER_FILE_SIZE) {
            return "썸네일 이미지 파일 크기는 최대 20MB까지만 허용됩니다.";
        }
        String extension = getFileExtension(imageFile.getOriginalFilename());
        if (!extension.equals("")) {
            if (!extension.matches("jpg|jpeg|png")) {
                return "썸네일 이미지 파일은 .jpg, .jpeg, .png 형식만 허용됩니다.";
            }
        }
        return returnString;
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }


    //동영상 목록
    @GetMapping("/video/list")
    public ModelAndView getVideoList(/*Model model*/) {
        ModelAndView mv = new ModelAndView("secret/secretVideoList");
        mv.addObject("videos", secretVideoService.getAllVideos());
        return mv; // 반환할 뷰의 이름
    }

    // 동영상 상세 페이지
    @GetMapping("/video/{video_id}")
    public ModelAndView getVideoDetail(@PathVariable Long video_id) throws UnsupportedEncodingException {
        // 동영상 ID로 정보를 조회
        Secret_videos video = secretVideoService.getVideoById(video_id);
        String filePath = video.getVideo_url();
        int lastIndexOfBackslash = filePath.lastIndexOf("\\");
        String fileName = filePath.substring(lastIndexOfBackslash + 1);
        System.out.println(fileName);

        String subtitlePath = video.getSubtitle_file_path();

        // 조회된 비디오 정보를 뷰에 전달
        ModelAndView mv = new ModelAndView("secret/secretVideoDetail");

        mv.addObject("video", video);
        mv.addObject("encodeUrl", fileName);
        mv.addObject("subtitlePath", subtitlePath);
        return mv; // 반환할 뷰의 이름
    }

    //동영상 목록
    @GetMapping("/video/manage")
    public String getVideoManageList(Model model) {
        model.addAttribute("videos", secretVideoService.getAllVideos());
        return "secret/secretVideoManageList"; // 반환할 뷰의 이름
    }
}
