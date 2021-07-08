package com.sparta.spring_week1_homework.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long textId;
    private String username;
    private String contents;
}
