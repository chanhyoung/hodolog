package com.hodolog.api.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
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
	
	@Lob
	private String content;
	
	@Builder
	public Course(String courseSlug, String title, String subtitle, String thumbnail, String video,
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
