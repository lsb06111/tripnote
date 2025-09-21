package edu.example.tripnote.domain.board;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardDetailDTO {
	//reviewContent
	private int reviewContentId;
	private String content;
	private boolean isDraft;
	private String imgSrc;
	private int orders; // 
	//tourLoc
	private int tourLocId;
	private String tourLocName;
	private String startTime;
	private String endTime;
	private int tourOrder;
	private String typeName;
	private int tourNth;
	//course
	private String courseTitle;
	private String startDate;
	private String endDate;
	private String areaName;
}
