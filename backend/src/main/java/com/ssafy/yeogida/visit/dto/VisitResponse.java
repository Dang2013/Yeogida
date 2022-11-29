package com.ssafy.yeogida.visit.dto;

import com.ssafy.yeogida.tour.domain.Tour;
import com.ssafy.yeogida.tour.dto.TourResponse;
import com.ssafy.yeogida.visit.domain.Visit;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VisitResponse {
	private Long id;
	private TourResponse tourResponse;
	private double score;

	@Builder
	private VisitResponse(Long id, Tour tour, Double score) {
		this.id = id;
		this.tourResponse = TourResponse.from(tour);
		this.score = score;
	}

	public static VisitResponse from(Visit visit) {
		return VisitResponse.builder()
			.id(visit.getId())
			.tour(visit.getTour())
			.score(visit.getScore())
			.build();
	}



}
