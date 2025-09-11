package edu.example.tripnote.domain.trip;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TourLocDTO {
	private int id;
	private String code;
	private String tourLocName;
	private int tourOrder;
	private String startTime;
	private String endTime;
	private int tourNth;
	private int timeTaken;
	private String imgSrc;
	private int courseId;
	private String typeName;
	private String noteContent;

}
