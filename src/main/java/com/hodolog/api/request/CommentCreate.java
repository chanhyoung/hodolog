package com.hodolog.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCreate {
    private String message;
    private Long userId;
}
