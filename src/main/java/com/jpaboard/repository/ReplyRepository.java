package com.jpaboard.repository;

import com.jpaboard.entity.MpReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<MpReply, Long> {
    // 특정 게시글에 대한 댓글 목록 조회
    List<MpReply> findByBoardVO_Bno(Long boardVOBno);
}
