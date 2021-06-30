package com.sparta.spring_week1_homework.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TextRepository extends JpaRepository<Text, Long> {
    List<Text> findAllByOrderByModifiedAtDesc(); // 찾아줘 모두 정렬해줘 수정한 기준으로 내림차순
    List<Text> findAllById(Long id); // 찾아줘 모두 정렬해줘 수정한 기준으로 내림차순
}