package edu.example.tripnote.domain.board;

import java.util.List;

import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardTemplateDTO {
	private List<List<TourLocDTO>> tourlocs;
	private CourseDTO course;
}
