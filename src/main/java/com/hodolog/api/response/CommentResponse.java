package com.hodolog.api.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 서비스 정책에 맞는 클래스
 */
@Getter
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String message;
    private String userId;
    private LocalDateTime createdAt;
}
