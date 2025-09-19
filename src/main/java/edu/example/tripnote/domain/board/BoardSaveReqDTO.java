package edu.example.tripnote.domain.board;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardSaveReqDTO {
	//board
	private Long boardId;
	private String boardTitle;
	private String intro;
	private int tourlocId;
	private int userId;
	private int courseId;
	private MultipartFile thumbnail;
	private String thumbnailSrc;
	//review_content
	private List<ReviewContentDTO> contents;
}
