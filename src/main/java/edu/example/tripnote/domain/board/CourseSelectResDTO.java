package edu.example.tripnote.domain.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseSelectResDTO {
	private int courseId;
	private String title;
	private String startDate;
	private String endDate;
	private int days;  
	private String loc;
	private String transport;
}
