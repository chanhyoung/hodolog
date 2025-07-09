package com.hodolog.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    private final String title;
    private final String category;
    private final String content;
	private final String[] tags;
	private final int readCount;
    private final int likeCount;
    private final int commentCount;
    private final int bookmarkCount;
    private final User user;    

    @Builder
    public PostEditor(String title, String category, String content, String[] tags,  
    		int readCount, int likeCount, int commentCount, int bookmarkCount, User user) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.tags = tags;
        this.readCount = readCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.bookmarkCount = bookmarkCount;
        this.user = user;
    }
}
