package edu.example.tripnote.domain.board;

import lombok.ToString;

import lombok.Setter;
import edu.example.tripnote.Constants;
import lombok.Getter;

@Getter
@Setter
@ToString
public class BoardParamDTO {
	private int page = 1;
	private int size = Constants.BOARD_PAGE_SIZE;
	private int offset;
	private String loc;
	private String order;
	private String keyword;

}
