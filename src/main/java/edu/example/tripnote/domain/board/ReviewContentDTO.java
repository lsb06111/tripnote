package edu.example.tripnote.domain.board;

import lombok.Data;

@Data
public class ReviewContentDTO {
	private int tourLocId;
	private String content;
	private String img;
}
