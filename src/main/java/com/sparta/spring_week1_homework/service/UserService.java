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
        // ?????? ID ?????? ??????
        Optional<User> found = userRepository.findByUsername(username);
        if (username.equals("null") || username.equals("admin")) {
            errorMessage = "???????????? ID?????????.";
            return errorMessage;
        }
        if (found.isPresent()) {
            errorMessage = "????????? ????????? ID??? ???????????????.";
            return errorMessage;
        }
        if (requestDto.getPassword().contains(username)) {
            errorMessage = "PW??? ID??? ???????????? ?????????????????????.";
            return errorMessage;
        }
        if (!requestDto.getPassword().equals(requestDto.getPasswordChecker())) {
            errorMessage = "PW ????????? ???????????????.";
            return errorMessage;
        }
        // ???????????? ?????????
        String password = passwordEncoder.encode(requestDto.getPassword());
        // ????????? ROLE ??????
        UserRole role = UserRole.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                errorMessage = "????????? ????????? ?????? ????????? ??????????????????.";
                return errorMessage;
            }
            role = UserRole.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
        return errorMessage;
    }

    public void kakaoLogin(String authorizedCode) {
        // ????????? OAuth2 ??? ?????? ????????? ????????? ?????? ??????
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        // DB ??? ????????? Kakao Id ??? ????????? ??????
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        if (kakaoUser == null) {

            // ????????? ????????? ????????????
            String kakaoTag = ":K";
            // username = ????????? nickname
            String username = nickname + kakaoTag;
            // password = ????????? Id + ADMIN TOKEN
            String password = kakaoId + ADMIN_TOKEN;
            // ???????????? ?????????
            String encodedPassword = passwordEncoder.encode(password);
            // ROLE = ?????????
            UserRole role = UserRole.USER;

            kakaoUser = new User(username, encodedPassword, role, kakaoId);
            userRepository.save(kakaoUser);
        }

        // ?????? ????????? ??????
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}