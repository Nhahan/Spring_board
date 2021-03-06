package com.sparta.spring_week1_homework.controller;

import com.sparta.spring_week1_homework.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username;
        if (userDetails == null) {
            username = "null";
        } else {
            username = userDetails.getUsername();
        }
        request.getSession().setAttribute("username", username);
        return "index";
    }

    @GetMapping("/user/indexMyTexts")
    public String readMyTexts() {
        return "indexMyTexts";
    }

    @GetMapping("/user/gindex")
    public String readForGuests() {
        return "gindex";
    }

    @GetMapping("/user/detail")
    public String readDetail() {
        return "detail";
    }
}