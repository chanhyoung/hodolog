package com.hodolog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.hodolog.api.response.CourseResponse;

@Mapper
public interface CourseMapper {
	public List<CourseResponse> selectCourses();
}
