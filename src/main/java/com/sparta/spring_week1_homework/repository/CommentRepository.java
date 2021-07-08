package com.sparta.spring_week1_homework.repository;

import com.sparta.spring_week1_homework.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByModifiedAtDesc(); // 찾아줘 모두 정렬해줘 수정한 기준으로 내림차순
    @Query
    List<Comment> findAllByTextId(Long textId);
}