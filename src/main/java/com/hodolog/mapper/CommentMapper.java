package com.hodolog.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hodolog.api.domain.Comment;

@Mapper
public interface CommentMapper {
	public List<Comment> findAllByPostId(Map<String, Long> params);
}
