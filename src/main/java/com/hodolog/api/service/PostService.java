package com.hodolog.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hodolog.api.auth.UserAuthUtil;
import com.hodolog.api.domain.Post;
import com.hodolog.api.domain.PostEditor;
import com.hodolog.api.domain.User;
import com.hodolog.api.exception.PostNotFound;
import com.hodolog.api.exception.UserNotFound;
import com.hodolog.api.repository.PostRepository;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.request.PostSearch;
import com.hodolog.api.response.PostResponse;
import com.hodolog.mapper.PostMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserAuthUtil userAuthUtil;

    public void write(PostCreate postCreate) {
        User user = userAuthUtil.getLoginUser()
            	.orElseThrow(UserNotFound::new);

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .category(postCreate.getCategory())
                .content(postCreate.getContent())
                .tags(postCreate.getTags())
                .readCount(0)
                .likeCount(0)
                .commentCount(0)
                .bookmarkCount(0)
                .user(user)
                .build();
        
        log.info("post: {}", post);
        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
        		.content(post.getContent())
                .tags(post.getTags())
                .readCount(post.getReadCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .bookmarkCount(post.getBookmarkCount())
                .build();
    }
    
    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<PostResponse> getList(String category) {
    	log.info("category: {}", category);
    	
        // 카테고리 파라미터 정리
        String cleanCategory = (category != null) ? category.trim() : null;
        if (cleanCategory != null && cleanCategory.isEmpty()) {
            cleanCategory = null;
        }
        
        return postMapper.selectPosts(cleanCategory);
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder
        		.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
