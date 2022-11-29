package com.ssafy.yeogida.tour.dto;

import java.util.List;

import com.ssafy.yeogida.tour.domain.Tour;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TourResponse {
	private Long id;
	private String name;
	private String address;
	private String category;
	private Long doId;
	private Long siId;
	private List<String> withKeyword;
	private List<String> tripKeyword;
	private List<String> keywords;

	@Builder
	private TourResponse(Long id, String name, String address, String category, Long doId, Long siId, List<String> withKeyword, List<String> tripKeyword, List<String> keywords) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.category = category;
		this.doId = doId;
		this.siId = siId;
		this.withKeyword = withKeyword;
		this.tripKeyword = tripKeyword;
		this.keywords = keywords;
	}

	public static TourResponse from(Tour tour) {
		return TourResponse.builder()
			.id(tour.getId())
			.name(tour.getName())
			.address(tour.getAddress())
			.category(tour.getCategory())
			.doId(tour.getADo().getId())
			.siId(tour.getSi().getId())
			.withKeyword(tour.keywordsToList(tour.getWithKeyword()))
			.tripKeyword(tour.keywordsToList(tour.getTripKeyword()))
			.keywords(tour.keywordsToList(tour.getKeywords()))
			.build();
	}
}
