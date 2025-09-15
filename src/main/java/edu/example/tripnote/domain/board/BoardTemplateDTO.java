package edu.example.tripnote.domain.board;

import java.util.List;

import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;
import lombok.Data;

@Data
public class BoardTemplateDTO {
	private List<List<TourLocDTO>> tourlocs;
	private CourseDTO course;
}
