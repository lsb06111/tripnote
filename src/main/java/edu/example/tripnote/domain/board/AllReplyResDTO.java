package edu.example.tripnote.domain.board;

import java.util.List;

import lombok.Data;

@Data
public class AllReplyResDTO {
	private List<ReplyDTO> parents;
	private List<ReplyDTO> childs;
}
