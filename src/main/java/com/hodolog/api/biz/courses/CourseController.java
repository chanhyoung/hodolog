package com.hodolog.api.biz.courses;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hodolog.api.response.CourseResponse;
import com.hodolog.api.service.CourseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CourseController {
	private final CourseService courseService;
	
	@GetMapping("/courses")
	public List<CourseResponse> getList() {
		return courseService.getList();
	}
	
	@GetMapping("/courses/{courseSlug}")
	public CourseResponse get(@PathVariable String courseSlug) {
		log.info(">>>courseSlug: {}", courseSlug);
		return courseService.get(courseSlug);
	}
}
