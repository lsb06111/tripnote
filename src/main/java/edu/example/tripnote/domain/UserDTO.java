package edu.example.tripnote.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private String username;
	private String password;
	private String email;
	private String name;
	private String nickname;
	private boolean profile_image;
}
