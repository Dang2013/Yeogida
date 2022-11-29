package com.ssafy.yeogida.region.dto;

import com.ssafy.yeogida.region.domain.Si;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiResponse {
	private Long id;
	private DoResponse doResponse;
	private String name;

	@Builder
	private SiResponse(Long id, DoResponse doResponse, String name) {
		this.id = id;
		this.doResponse = doResponse;
		this.name = name;
	}

	public static SiResponse from(Si si) {
		return SiResponse.builder()
			.id(si.getId())
			.doResponse(DoResponse.from(si.getADo()))
			.name(si.getName())
			.build();
	}
}
