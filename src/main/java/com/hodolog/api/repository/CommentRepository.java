package com.hodolog.api.repository;

import com.hodolog.api.domain.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  public List<Comment> findByPostId(Long postId);
}
