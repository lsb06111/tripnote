package edu.example.tripnote.domain.trip;

import lombok.Getter;
import lombok.Setter;


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
    private String typeName;
}
