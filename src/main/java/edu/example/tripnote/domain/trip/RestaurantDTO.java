package edu.example.tripnote.domain.trip;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class RestaurantDTO {
	private String restaurant_name;
	private String mapx;
	private String mapy;
	private String phone;
	private String addr1;
	private String detail;
}
