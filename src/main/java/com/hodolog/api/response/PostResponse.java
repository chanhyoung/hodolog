package com.hodolog.api.response;

import com.hodolog.api.domain.Post;

import lombok.Builder;
import lombok.Getter;

/**
 * 서비스 정책에 맞는 클래스
 */
@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private String category;
    private final String content;
//    private final String[] tags;
    private final int readCount;
    private final int likeCount;
    private final int commentCount;
    private final int bookmarkCount;
    
    // 생성자 오버로딩
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.content = post.getContent();
//        this.tags = post.getTags();
        this.readCount = post.getReadCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.bookmarkCount = post.getBookmarkCount();
    }

    @Builder
    public PostResponse(Long id, String title, String category, String content, 
//    		String[] tags, 
    		int readCount, int likeCount, int commentCount, int bookmarkCount) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.category = category;
        this.content = content;
//        this.tags = tags;
        this.readCount = readCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.bookmarkCount = bookmarkCount;
    }
}
