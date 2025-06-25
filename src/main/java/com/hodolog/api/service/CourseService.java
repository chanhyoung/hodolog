package com.hodolog.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hodolog.api.domain.Course;
import com.hodolog.api.exception.CourseNotFound;
import com.hodolog.api.repository.CourseRepository;
import com.hodolog.api.response.CourseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
	private final CourseRepository courseRepository;
	
	public List<CourseResponse> getList() {
		return courseRepository.findAll().stream()
				.map(course -> new CourseResponse(course))
				.collect(Collectors.toList());
	}
	
	public CourseResponse get(String courseSlug) {
		Course course = courseRepository.findByCourseSlug(courseSlug)
				.orElseThrow(() -> new CourseNotFound());
		
		CourseResponse response = new CourseResponse(course);
		
		return response;
	}
}
