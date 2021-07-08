package com.sparta.spring_week1_homework.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    } // 패스워드 암호화

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                // image 폴더를 login 없이 허용
                .antMatchers("/image/**").permitAll()
                // css 폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/login/error").permitAll()
                .antMatchers("/user/signup").permitAll()
                .antMatchers("/user/gindex").permitAll()
                .antMatchers("/user/kakao/callback").permitAll()
                .antMatchers("/api/comments").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                // 게스트에게 보여주기
                .antMatchers("/api/texts").permitAll()
                // 그 외 모든 요청은 인증과정 필요
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/user/login") // 로그인 위치
                .loginProcessingUrl("/user/login") // 로그인 실패 위치
                .defaultSuccessUrl("/")
                .failureUrl("/user/login/error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/user/logout") // 시큐리티가 로그아웃 자동으로 진행해줌!
                .permitAll()
                .and()
                .exceptionHandling()
                // 인가되지 않았을 때 아래의 주소로 이동
                .accessDeniedPage("/user/forbidden");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}