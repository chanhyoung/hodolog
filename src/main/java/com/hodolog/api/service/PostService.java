package com.hodolog.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                .createdAt(post.getCreatedAt())
                .build();
    }
    
    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<PostResponse> getList(PostSearch request) {
    	Map<String, Object> params = new HashMap<>();
   		params.put("category", request.getCategory());
   		params.put("tags", request.getTags());
   		params.put("sort", request.getSort());
        
        return postMapper.selectPosts(params);
    }

    // public List<PostResponse> getList(PostSearch postSearch) {
    //     return postRepository.getList(postSearch).stream()
    //             .map(PostResponse::new)
    //             .collect(Collectors.toList());
    // }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder
        		.title(postEdit.getTitle())
        		.category(postEdit.getCategory())
                .content(postEdit.getContent())
                .tags(postEdit.getTags())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
