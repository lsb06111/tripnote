package edu.example.tripnote.domain.board;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardSaveReqDTO {
	private Long boardId;
	private String title;
	private String intro;
	private int userId;
	private int courseId;
	private String thumbnail;
	private List<ReviewContentDTO> contents;
}
