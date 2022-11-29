package com.ssafy.yeogida.region.dto;

import com.ssafy.yeogida.region.domain.Do;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DoResponse {
	private Long id;
	private String name;

	@Builder
	private DoResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static DoResponse from(Do aDo) {
		return DoResponse.builder()
			.id(aDo.getId())
			.name(aDo.getName())
			.build();
	}
}
