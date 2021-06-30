package com.sparta.spring_week1_homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringWeek1HomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWeek1HomeworkApplication.class, args);
    }

}
