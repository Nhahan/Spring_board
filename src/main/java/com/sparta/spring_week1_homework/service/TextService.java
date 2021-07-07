package com.sparta.spring_week1_homework.service;

import com.sparta.spring_week1_homework.domain.Text;
import com.sparta.spring_week1_homework.repository.TextRepository;
import com.sparta.spring_week1_homework.dto.TextRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class TextService {

    private final TextRepository textRepository;

    @Transactional // DB에 반영돼야해!
    public Long update(Long id, TextRequestDto requestDto) {
        Text text = textRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        text.update(requestDto);
        return text.getId();
    }
}