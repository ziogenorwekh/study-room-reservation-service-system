package com.choongang.studyreservesystem.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostDto {
    private String title;
    private String username;
    private String content;
}
