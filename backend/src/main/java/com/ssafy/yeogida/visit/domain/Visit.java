package com.ssafy.yeogida.visit.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ssafy.yeogida.member.domain.Member;
import com.ssafy.yeogida.tour.domain.Tour;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Visit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tour_id")
	private Tour tour;
	private Double score;

	@Builder
	private Visit(Member member, Tour tour, double score) {
		this.member = member;
		this.tour = tour;
		this.score = score;
	}

	public static Visit from(Member member, Tour tour, double score) {
		return Visit.builder()
			.member(member)
			.tour(tour)
			.score(score)
			.build();
	}

	public void setScore(Double score) {
		this.score = score;
	}
}
