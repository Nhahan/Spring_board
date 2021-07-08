package com.sparta.spring_week1_homework.controller;

import com.sparta.spring_week1_homework.domain.Comment;
import com.sparta.spring_week1_homework.dto.CommentRequestDto;
import com.sparta.spring_week1_homework.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentRepository commentRepository; // CRUD하려면 DB 필요

    @GetMapping("/api/comments") // 댓글 조회
    public List<Comment> readComment() {
        return commentRepository.findAllByOrderByModifiedAtDesc();
    }

    @GetMapping("/api/counter/{Id}") // 댓글 갯수 조회
    public int readCommentCounter(@PathVariable Long Id, Model model) {
        List<Comment> list = commentRepository.findAllByTextId(Id);
        model.addAttribute("counter", list.size());
        return list.size();
    }

    @PostMapping("/api/comments") // 댓글 생성
    public Comment createComment(@RequestBody CommentRequestDto requestDto) {
        Comment comment = new Comment(requestDto);
        return commentRepository.save(comment);
    }

    @DeleteMapping("/api/comments/{id}")
    public Long deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
        return id;
    }
}
