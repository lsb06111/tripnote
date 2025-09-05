package edu.example.tripnote.domain.vo;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class BoardVO {
	private int id;
	private String title;
	private String content;
	private Boolean is_private;
	private Boolean is_visible;
	private int userId;
	private int courseId;
}
