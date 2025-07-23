package com.hodolog.api.service;

import com.hodolog.api.domain.Comment;
import com.hodolog.api.domain.Post;
import com.hodolog.api.domain.User;
import com.hodolog.api.exception.PostNotFound;
import com.hodolog.api.exception.UserNotFound;
import com.hodolog.api.repository.CommentRepository;
import com.hodolog.api.repository.PostRepository;
import com.hodolog.api.repository.UserRepository;
import com.hodolog.api.request.CommentCreate;
import com.hodolog.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public void create(Long postId, CommentCreate commentCreate) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        User user = userRepository.findById(commentCreate.getUserId())
                .orElseThrow(UserNotFound::new);

        Comment comment = Comment.builder()
                .message(commentCreate.getMessage())
                .post(post)
                .user(user)
                .build();

        commentRepository.save(comment);
    }

    public List<Comment> findAllByPostId(Long postId) {
        Map<String, Long> params = new HashMap<>();
        params.put("postId", postId);
        return commentMapper.findAllByPostId(params);
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
