package com.jpaboard.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jpaboard.entity.Videos;
import com.jpaboard.repository.VideoRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    private final String videoUploadDirectory = "E:/videoUploads"; // 동영상 파일 업로드 경로
    private final String subUploadDirectory = "E:/videoUploads";   // 자막 파일 업로드 경로
    private final String imageUploadDirectory = "E:/images";       // 썸네일 이미지 파일 업로드 경로

    private static final long MAX_VIDEO_SIZE = 5_500L * 1024 * 1024; // 동영상 최대 5.5GB
    private static final long MAX_OTHER_FILE_SIZE = 20L * 1024 * 1024; // 썸네일/자막 최대 20MB

    public Videos saveVideo(MultipartFile videoFile, MultipartFile subtitle, MultipartFile thumbnailFile, String title,
                            String description, String genre, Date release_date, Integer duration,
                            String language, String age_rating) throws IOException {

//        // 동영상 파일 크기 및 확장자 검증
//        validateVideoFile(videoFile);
        // 자막 파일 크기 및 확장자 검증
//        validateSubtitleFile(subtitle);
//        // 썸네일 파일 크기 및 확장자 검증
//        validateImageFile(thumbnailFile);

        // 동영상 파일 저장
        String videoFileName = videoFile.getOriginalFilename();
        File videoTargetFile = new File(videoUploadDirectory, videoFileName);
        videoFile.transferTo(videoTargetFile);

        // 자막 파일 저장
        String subFilename = subtitle.getOriginalFilename();
        String subSavedFilename = System.currentTimeMillis() + "_" + subFilename;
        File subTargetFile = new File(subUploadDirectory, subSavedFilename);
        subtitle.transferTo(subTargetFile);

        // 썸네일 파일 저장
        String originalFilename = thumbnailFile.getOriginalFilename();
        String savedFilename = System.currentTimeMillis() + "_" + originalFilename;
        File thumbnailTargetFile = new File(imageUploadDirectory, savedFilename);
        thumbnailFile.transferTo(thumbnailTargetFile);

        // 비디오 정보 저장
        Videos video = new Videos();
        video.setTitle(title);
        video.setDescription(description);
        video.setGenre(genre);
        video.setRelease_date(release_date);
        video.setDuration(duration);
        video.setLanguage(language);
        video.setAge_rating(age_rating);

        // 클라이언트에서 접근 가능한 URL 경로 저장
        video.setThumbnail_url(savedFilename); // 썸네일 파일 경로
        video.setSubtitle_file_path(subSavedFilename); // 자막 파일 경로
        video.setVideo_url(videoTargetFile.getAbsolutePath()); // 동영상 파일 경로
        video.setCreated_at(new java.sql.Timestamp(System.currentTimeMillis()));
        video.setUpdated_at(new java.sql.Timestamp(System.currentTimeMillis()));

        return videoRepository.save(video);
    }

    // 자막 파일 검증
    private void validateSubtitleFile(MultipartFile subtitle) {
        if (subtitle.getSize() > MAX_OTHER_FILE_SIZE) {
            throw new IllegalArgumentException("자막 파일 크기는 최대 20MB까지만 허용됩니다.");
        }
        String extension = getFileExtension(subtitle.getOriginalFilename());
        if (!"vtt".equalsIgnoreCase(extension)) {
            throw new IllegalArgumentException("자막 파일은 .vtt 형식만 허용됩니다.");
        }
    }

    // 썸네일 이미지 파일 검증
    private void validateImageFile(MultipartFile imageFile) {
        if (imageFile.getSize() > MAX_OTHER_FILE_SIZE) {
            throw new IllegalArgumentException("썸네일 이미지 파일 크기는 최대 20MB까지만 허용됩니다.");
        }
        String extension = getFileExtension(imageFile.getOriginalFilename());
        if (!extension.matches("jpg|jpeg|png")) {
            throw new IllegalArgumentException("썸네일 이미지 파일은 .jpg, .jpeg, .png 형식만 허용됩니다.");
        }
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    public Iterable<Videos> getAllVideos() {
        return videoRepository.findAll();
    }

    // ID로 동영상 조회
    public Videos getVideoById(Long video_id) {
        return videoRepository.findById(video_id)
                .orElseThrow(() -> new RuntimeException("동영상을 찾을 수 없습니다. ID: " + video_id));
    }
}
