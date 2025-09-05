package edu.example.tripnote.domain;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class TripInfoDTO {

	private String startDate;
	private String endDate;
	private AreaVO tripDest;
}
