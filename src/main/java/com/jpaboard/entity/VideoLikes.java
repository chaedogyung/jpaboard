package com.jpaboard.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(
        name = "video_likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userid", "video_id"})
        }
)
public class VideoLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_likes_seq_gen")
    @SequenceGenerator(name = "video_likes_seq_gen", sequenceName = "video_likes_seq", allocationSize = 1)
    @Column(name = "like_id")
    private Long like_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false, foreignKey = @ForeignKey(name = "fk_video_likes_user"))
    private Member userid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false, foreignKey = @ForeignKey(name = "fk_video_likes_video"))
    private Videos video;

    @Column(name = "liked_at", nullable = false, updatable = false)
    private LocalDateTime liked_at;

    @PrePersist
    protected void onCreate() {
        this.liked_at = LocalDateTime.now();
    }
}