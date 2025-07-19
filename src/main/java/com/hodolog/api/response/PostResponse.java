package com.hodolog.api.response;

import java.time.LocalDateTime;

import com.hodolog.api.domain.Post;
import com.hodolog.api.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 서비스 정책에 맞는 클래스
 */
@Getter
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String category;
    private String content;
    private String[] tags;
    private int readCount;
    private int likeCount;
    private int commentCount;
    private int bookmarkCount;
    private Long userId;
    private LocalDateTime createdAt;
    
    // 생성자 오버로딩
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.content = post.getContent();
        this.tags = post.getTags();
        this.readCount = post.getReadCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.bookmarkCount = post.getBookmarkCount();
        this.userId = post.getUser().getId();
        this.createdAt = post.getCreatedAt();
    }

    @Builder
    public PostResponse(Long id, String title, String category, String content, String[] tags, 
    		int readCount, int likeCount, int commentCount, int bookmarkCount, Long userId, LocalDateTime createdAt) {
        this.id = id;
        this.title = (title != null) ? title.substring(0, Math.min(title.length(), 10)) : null;
        this.category = category;
        this.content = content;
        this.tags = tags;
        this.readCount = readCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.bookmarkCount = bookmarkCount;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
