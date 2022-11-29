package com.ssafy.yeogida.visit.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VisitRegistRequest {
	private String jwtToken;
	private Long tourId;
	private Double userScore;
}
