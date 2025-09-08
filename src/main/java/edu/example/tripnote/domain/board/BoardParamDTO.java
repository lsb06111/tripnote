package edu.example.tripnote.domain.board;

import lombok.ToString;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
@ToString
public class BoardParamDTO {
	private String loc;
	private String order;
	private String keyword;

}
