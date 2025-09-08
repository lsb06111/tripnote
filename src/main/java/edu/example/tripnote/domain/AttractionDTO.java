package edu.example.tripnote.domain;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class AttractionDTO {
	private String contentId; // 콘텐츠 ID
    private String title;
    private String addr1;
    private String firstimage;
    private String mapx;
    private String mapy;
    private String overview;
    private String locations;
}
