package com.hodolog.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class PostEdit {

    @NotBlank(message = "타이틀을 입력하세요.")
    private String title;
    
    private String category;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;
    
    private String[] tags;

    @Builder
    public PostEdit(String title, String category, String content, String[] tags) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.tags = tags;
    }
}
