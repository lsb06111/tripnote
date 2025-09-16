package edu.example.tripnote.domain.trip;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembersDTO {
	private int fromUserId;
	private int toUserId;
	private Long courseId;
	private String invitationUrl;
}
