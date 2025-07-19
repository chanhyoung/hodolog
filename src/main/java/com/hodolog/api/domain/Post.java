package com.hodolog.api.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import com.vladmihalcea.hibernate.type.array.StringArrayType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Entity
@TypeDef(
    name = "string-array",
    typeClass = StringArrayType.class
)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    private String category;

	@Column(length = 60000)
	private String content;
	
    @Column(name = "tags", columnDefinition = "text[]")
    @org.hibernate.annotations.Type(type = "string-array")
    private String[] tags;
	
    private int readCount;

    private int likeCount;
    
    private int commentCount;
    
    private int bookmarkCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Builder
    public Post(String title, String category, String content, String[] tags, 
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

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .category(category)
        		.content(content)
                .tags(tags)
                .readCount(readCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .bookmarkCount(bookmarkCount)
                .user(user);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        category = postEditor.getCategory();
        content = postEditor.getContent();
        tags = postEditor.getTags();
        readCount = postEditor.getReadCount();
        likeCount = postEditor.getLikeCount();
        commentCount = postEditor.getCommentCount();
        bookmarkCount = postEditor.getBookmarkCount();
        user = postEditor.getUser();
    }
}