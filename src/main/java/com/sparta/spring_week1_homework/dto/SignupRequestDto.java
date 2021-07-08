package com.sparta.spring_week1_homework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SignupRequestDto {

    @NotBlank(message = "ID는 필수 입력 값입니다.")
    @Pattern(regexp="^[a-zA-Z]{1}[a-zA-Z0-9_]{2,12}$",
            message = "ID는 3글자 이상 12글자 이하의 값이어야 합니다.")
    private String username;

    @NotBlank(message = "PW는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{4,16}",
            message = "비밀번호는 영문 대, 소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 4자 ~ 16자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "PW를 확인해주세요.")
    private String passwordChecker;
    private boolean admin = false;
    private String adminToken = "";
}