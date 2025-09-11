package edu.example.tripnote.domain.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseSelectRsps {
	private String title;
	private String days;  
	private String loc;

	public void setDays(int days) {
		this.days = (days-1) + "박 "+ days + "일";
	}
}
