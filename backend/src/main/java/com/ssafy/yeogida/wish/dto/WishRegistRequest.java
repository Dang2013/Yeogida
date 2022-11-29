package com.ssafy.yeogida.wish.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WishRegistRequest {
	private String jwtToken;
	private Long tourId;
}
