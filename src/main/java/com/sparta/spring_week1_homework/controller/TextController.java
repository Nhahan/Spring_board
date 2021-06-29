package com.sparta.spring_week1_homework.controller;

import com.sparta.spring_week1_homework.domain.Text;
import com.sparta.spring_week1_homework.domain.TextRepository;
import com.sparta.spring_week1_homework.domain.TextRequestDto;
import com.sparta.spring_week1_homework.service.TextService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TextController {

    private final TextRepository textRepository; // CRUD하려면 DB 필요
    private final TextService textService; // 업데이트를 위해서는 Service가 필요

    @PostMapping("/api/texts") // 게시글 생성
    public Text createText(@RequestBody TextRequestDto requestDto) { // POST와 PUT에는 @RequestBody 있어야함
        Text text = new Text(requestDto);
        return textRepository.save(text);
    }

    @GetMapping("/api/texts") // 게시글 조회
    public List<Text> readText() {
        return textRepository.findAllByOrderByModifiedAtDesc();
    }

//    @PutMapping

    @DeleteMapping("/api/texts/{id}")
    public Long deleteText(@PathVariable Long id) {
        textRepository.deleteById(id);
        return id;
    }
}