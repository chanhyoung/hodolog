package com.hodolog.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hodolog.api.response.PostResponse;

@Mapper
public interface PostMapper {
	public List<PostResponse> selectPosts(Map<String, String> params);
}
