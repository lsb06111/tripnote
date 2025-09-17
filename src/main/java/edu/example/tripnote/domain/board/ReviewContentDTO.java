package edu.example.tripnote.domain.board;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ReviewContentDTO {
	private int id;	
	private Long boardId;
	private String title;
	private String content;
	private int orders;
	private boolean isDraft;
	private String imgSrc;
	private MultipartFile file; // FormData에서 매핑될 필드
}
