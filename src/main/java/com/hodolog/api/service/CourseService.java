package com.hodolog.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hodolog.api.domain.Course;
import com.hodolog.api.exception.CourseNotFound;
import com.hodolog.api.repository.CourseRepository;
import com.hodolog.api.response.CourseResponse;
import com.hodolog.mapper.CourseMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
	private final CourseRepository courseRepository;
	private final CourseMapper courseMapper;
	
	public List<CourseResponse> getList() {
//		return courseRepository.findAll().stream()
//				.map(course -> new CourseResponse(course))
//				.collect(Collectors.toList());
		
		return courseMapper.selectCourses();
	}
	
	public CourseResponse get(String courseSlug) {
		Course course = courseRepository.findByCourseSlug(courseSlug)
				.orElseThrow(() -> new CourseNotFound());
		
		CourseResponse response = new CourseResponse(course);
		
		return response;
	}
}
