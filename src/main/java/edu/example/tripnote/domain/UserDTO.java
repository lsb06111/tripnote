package edu.example.tripnote.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private int id;
	private String username;
	private String password;
	private String email;
    private String users_name;
	private String nickname;
    private String profileImage;
}
