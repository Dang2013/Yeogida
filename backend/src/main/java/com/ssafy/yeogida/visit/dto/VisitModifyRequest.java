package com.ssafy.yeogida.visit.dto;

import lombok.Getter;

@Getter
public class VisitModifyRequest {
	private String jwtToken;
	private Long id;
	private Double score;
}
