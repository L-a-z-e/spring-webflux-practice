package com.laze.springwebfluxpractice.dto;

import lombok.Data;

@Data
public class PostR2dbcCreateRequest {
    private Long userId;
    private String title;
    private String content;
}
