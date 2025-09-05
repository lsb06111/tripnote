package edu.example.tripnote.domain;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Component
@Getter
@AllArgsConstructor
public class AreaVO {
	
	private int id;
	private String name;
}
