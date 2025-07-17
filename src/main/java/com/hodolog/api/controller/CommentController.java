package com.hodolog.api.controller;

import com.hodolog.api.domain.Comment;
import com.hodolog.api.request.CommentCreate;
import com.hodolog.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public List<Comment> getComments(@PathVariable Long postId) {
        return commentService.findAllByPostId(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    public void create(@PathVariable Long postId, @RequestBody CommentCreate request) {
        commentService.create(postId, request);
    }

    @DeleteMapping("/comments/{commentId}")
    public void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }
}
