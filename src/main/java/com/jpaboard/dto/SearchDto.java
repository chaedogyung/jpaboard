package com.jpaboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private String title;
    private String writer;
    private String content;
    private String fullSearch;
}
