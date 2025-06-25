package com.hodolog.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hodolog.api.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
	Optional<Course> findByCourseSlug(String courseSlug);
}
