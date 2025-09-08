package edu.example.tripnote.domain.board;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDTO {
	private int id;
	private String title;
	private String boardContent;
	private String nickname;
	private Date createdAt;
	private Date modifiedAt;
	private String areaName;
	private int likes;
	private String thumbnail;
}
