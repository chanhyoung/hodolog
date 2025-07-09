package com.hodolog.api.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.hodolog.api.exception.InvalidRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력하세요.")
    private String title;

    private String category;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;
    
    private String[] tags;
	
    private Long userId;

    @Builder
    public PostCreate(String title, String category, String content, String[] tags) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.tags = tags;
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }
}
