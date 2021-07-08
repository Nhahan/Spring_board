package com.sparta.spring_week1_homework.domain;

import com.sparta.spring_week1_homework.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor // 기본생성자를 만듭니다.
@Getter
@Entity // 테이블과 연계됨을 스프링에게 알려줍니다.
public class Comment extends Timestamped { // 생성,수정 시간을 자동으로 만들어줍니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private Long textId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

    public Comment(CommentRequestDto requestDto) {
        this.textId = requestDto.getTextId();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}