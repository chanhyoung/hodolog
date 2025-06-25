package com.hodolog.api.response;

import com.hodolog.api.domain.Course;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseResponse {
	private String courseSlug;
	
	private String title;
	
	private String subtitle;
	
	private String thumbnail;
	
	private String video;
	
	private Long rating;
	
	private int reviewsCount;
	
	private int studentCount;
	
	private String reviewsUrl;
	
	private String  inflearnUrl;
	
	private String gymcodingUrl;
	
	private String content;
	
	public CourseResponse(Course course) {
		this.courseSlug = course.getCourseSlug();
		this.title = course.getTitle();
		this.subtitle = course.getSubtitle();
		this.thumbnail = course.getThumbnail();
		this.video = course.getVideo();
		this.rating = course.getRating();
		this.reviewsCount = course.getReviewsCount();
		this.studentCount = course.getStudentCount();
		this.reviewsUrl = course.getReviewsUrl();
		this.inflearnUrl = course.getInflearnUrl();
		this.gymcodingUrl = course.getGymcodingUrl();
		this.content = course.getContent();
	}
	
	@Builder
	public CourseResponse(String courseSlug, String title, String subtitle, String thumbnail, String video,
				Long rating, int reviewsCount, int studentCount, String reviewsUrl, String  inflearnUrl,
				String gymcodingUrl, String content) {
		this.courseSlug = courseSlug;
		this.title = title;
		this.subtitle = subtitle;
		this.thumbnail = thumbnail;
		this.video = video;
		this.rating = rating;
		this.reviewsCount = reviewsCount;
		this.studentCount = studentCount;
		this.reviewsUrl = reviewsUrl;
		this.inflearnUrl = inflearnUrl;
		this.gymcodingUrl = gymcodingUrl;
		this.content = content;
	}
}
