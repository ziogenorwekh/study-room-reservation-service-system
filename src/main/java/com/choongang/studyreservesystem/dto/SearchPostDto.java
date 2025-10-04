package com.choongang.studyreservesystem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPostDto {
    private String keyword;
    private String searchType; // "title", "content", "all"
}
