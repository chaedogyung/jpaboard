package com.jpaboard.service;

import com.jpaboard.dto.VideoDetailDto;
import com.jpaboard.entity.Member;
import com.jpaboard.entity.VideoLikes;
import com.jpaboard.repository.LikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.jpaboard.entity.Videos;
import com.jpaboard.repository.VideoRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final LikeRepository likeRepository;


    private final String videoUploadDirectory = "E:/videoUploads"; // 동영상 파일 업로드 경로
    private final String subUploadDirectory = "E:/videoUploads";   // 자막 파일 업로드 경로
    private final String imageUploadDirectory = "E:/images";       // 썸네일 이미지 파일 업로드 경로


    public Videos saveVideo(MultipartFile videoFile, MultipartFile subtitle, MultipartFile thumbnailFile, String title,
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

    public Iterable<Videos> getAllVideos() {
        return videoRepository.findAll();
    }

    // ID로 동영상 조회
    public VideoDetailDto getVideoDetailWithLikeCount(Long video_id, String userId) {
        return videoRepository.findVideoDetailWithLikeCount(video_id, userId);
    }

    public Integer addLike(Long videoId, String userId) {
        // VideoLikes 엔티티 생성
        VideoLikes like = new VideoLikes();

        //관련된 Videos와 Member 엔티티를 설정
        Videos videos = new Videos();
        videos.setVideoId(videoId);
        like.setVideo(videos);

        Member member = new Member();
        member.setUserid(userId);
        like.setUserid(member);

        like.setLiked_at(LocalDateTime.now());
        likeRepository.save(like);
        return 1;
    }

    public Integer cancelLike(Long videoId, String userId) {
        // 사용자 ID와 비디오 ID를 기준으로 좋아요 데이터를 삭제
        int result = likeRepository.deleteLikeByUserIdAndVideoId(userId, videoId);
        if (result == 1) {
            return 0;
        } else {
            return 1;
        }
    }

    // 특정 비디오의 좋아요 수 가져오기
    public long getLikesCountByVideoId(Long videoId) {
        return likeRepository.countByVideo_VideoId(videoId);
    }
}
