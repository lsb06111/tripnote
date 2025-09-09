package edu.example.tripnote.domain.trip;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CourseDTO {
	private int id;
	private String title;
	private String createdAt;
	private String startDate;
	private String endDate;
	private boolean isVisible;
	private int userId;
	private int areaId;
}
