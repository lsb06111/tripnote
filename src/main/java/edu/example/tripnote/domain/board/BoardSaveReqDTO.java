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
	private NewBoardDTO boardDTO;
	private List<ReviewContentDTO> contents;

}
