package edu.example.tripnote.domain.board;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ReviewContentDTO {
	private int tourLocId;
	private String content;
	private List<MultipartFile> files; // FormData에서 매핑될 필드
	private String imgSrc;
}
