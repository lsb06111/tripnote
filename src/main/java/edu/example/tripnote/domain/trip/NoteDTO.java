package edu.example.tripnote.domain.trip;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDTO {
	private Long id;
	private String noteContent;
	//private Date noteDay;
	private Long tourLocId;
}
