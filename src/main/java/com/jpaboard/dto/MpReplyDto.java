package com.jpaboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MpReplyDto {
    private Long reRno;
    private Long parentReRno;

    private String reContent;

    private String reWriter;

    private LocalDateTime reRegDate;

    private Long bno;

}
