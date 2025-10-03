package com.choongang.studyreservesystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePsotDto {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    private String content;

    // 동시수정 방지용
    private Long version;
}
