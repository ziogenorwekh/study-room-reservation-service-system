package com.choongang.studyreservesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // 💡 Import 추가

@EnableJpaAuditing // 💡
@SpringBootApplication
public class StudyReserveSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyReserveSystemApplication.class, args);
    }
}