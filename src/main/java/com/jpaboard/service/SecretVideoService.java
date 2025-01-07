package com.jpaboard.service;

import com.jpaboard.entity.Secret_videos;
import com.jpaboard.repository.SecretVideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
public class SecretVideoService {

    private final SecretVideoRepository secretVideoRepository;

    private final String videoUploadDirectory = "E:/secretVideoUploads"; // 동영상 파일 업로드 경로
    private final String subUploadDirectory = "E:/secretVideoUploads";   // 자막 파일 업로드 경로
    private final String imageUploadDirectory = "E:/secretimages";       // 썸네일 이미지 파일 업로드 경로


    public Secret_videos saveVideo(MultipartFile videoFile, MultipartFile subtitle, MultipartFile thumbnailFile, String title,
                            String description, String genre, Date release_date, Integer duration,
                            String language, String age_rating) throws IOException {

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
        Secret_videos secret_video = new Secret_videos();
        secret_video.setTitle(title);
        secret_video.setDescription(description);
        secret_video.setGenre(genre);
        secret_video.setRelease_date(release_date);
        secret_video.setDuration(duration);
        secret_video.setLanguage(language);
        secret_video.setAge_rating(age_rating);

        // 클라이언트에서 접근 가능한 URL 경로 저장
        secret_video.setThumbnail_url(savedFilename); // 썸네일 파일 경로
        secret_video.setSubtitle_file_path(subSavedFilename); // 자막 파일 경로
        secret_video.setVideo_url(videoTargetFile.getAbsolutePath()); // 동영상 파일 경로
        secret_video.setCreated_at(new java.sql.Timestamp(System.currentTimeMillis()));
        secret_video.setUpdated_at(new java.sql.Timestamp(System.currentTimeMillis()));

        return secretVideoRepository.save(secret_video);
    }

    public Iterable<Secret_videos> getAllVideos() {
        return secretVideoRepository.findAll();
    }

    // ID로 동영상 조회
    public Secret_videos getVideoById(Long video_id) {
        return secretVideoRepository.findById(video_id)
                .orElseThrow(() -> new RuntimeException("동영상을 찾을 수 없습니다. ID: " + video_id));
    }
}
