package com.sparta.spring_week1_homework.service;

import com.sparta.spring_week1_homework.domain.Text;
import com.sparta.spring_week1_homework.dto.CommentRequestDto;
import com.sparta.spring_week1_homework.repository.CommentRepository;
import com.sparta.spring_week1_homework.repository.TextRepository;
import com.sparta.spring_week1_homework.dto.TextRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.stream.events.Comment;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

//    @Transactional // DB에 반영돼야해!
//    public Long update(Long id, CommentRequestDto commentDto) {
//        Comment comment = commentRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
//        );
//        comment.update(requestDto);
//        return text.getId();
//    }
}