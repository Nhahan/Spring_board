package com.sparta.spring_week1_homework.service;

import com.sparta.spring_week1_homework.domain.User;
import com.sparta.spring_week1_homework.domain.UserRole;
import com.sparta.spring_week1_homework.dto.SignupRequestDto;
import com.sparta.spring_week1_homework.repository.UserRepository;
import com.sparta.spring_week1_homework.security.UserDetailsImpl;
import com.sparta.spring_week1_homework.security.kakao.KakaoOAuth2;
import com.sparta.spring_week1_homework.security.kakao.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, KakaoOAuth2 kakaoOAuth2) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    public String registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String errorMessage = "";
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (username.equals("null")) {
            errorMessage = "부적절한 ID입니다.";
            return errorMessage;
        }
        if (found.isPresent()) {
            errorMessage = "중복된 사용자 ID가 존재합니다.";
            return errorMessage;
        }
        if (requestDto.getPassword().contains(username)) {
            errorMessage = "PW와 ID에 중복값이 포함되었습니다.";
            return errorMessage;
        }
        if (!requestDto.getPassword().equals(requestDto.getPasswordChecker())) {
            errorMessage = "PW 확인이 틀렸습니다.";
            return errorMessage;
        }
        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                errorMessage = "관리자 암호가 틀려 등록이 불가능합니다.";
                return errorMessage;
            }
            role = UserRole.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
        return errorMessage;
    }

    public void kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        if (kakaoUser == null) {

            // 카카오 정보로 회원가입
            String kakaoTag = ":K";
            // username = 카카오 nickname
            String username = nickname + kakaoTag;
            // password = 카카오 Id + ADMIN TOKEN
            String password = kakaoId + ADMIN_TOKEN;
            // 패스워드 인코딩
            String encodedPassword = passwordEncoder.encode(password);
            // ROLE = 사용자
            UserRole role = UserRole.USER;

            kakaoUser = new User(username, encodedPassword, role, kakaoId);
            userRepository.save(kakaoUser);
        }

        // 강제 로그인 처리
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}