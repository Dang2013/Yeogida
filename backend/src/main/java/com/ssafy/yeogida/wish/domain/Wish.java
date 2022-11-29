package com.ssafy.yeogida.wish.domain;

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
public class Wish {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tour_id")
	private Tour tour;

	@Builder
	private Wish(Member member, Tour tour) {
		this.member = member;
		this.tour = tour;
	}

	public static Wish from(Member member, Tour tour) {
		return Wish.builder()
			.member(member)
			.tour(tour)
			.build();
	}
}
