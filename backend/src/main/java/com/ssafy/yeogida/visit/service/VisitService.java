package com.ssafy.yeogida.visit.service;

import static com.ssafy.yeogida.member.MemberConstant.*;
import static com.ssafy.yeogida.tour.TourConstant.*;
import static com.ssafy.yeogida.visit.VisitConstant.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yeogida.member.JwtTokenProvider;
import com.ssafy.yeogida.member.domain.Member;
import com.ssafy.yeogida.member.repository.MemberRepository;
import com.ssafy.yeogida.tour.domain.Tour;
import com.ssafy.yeogida.tour.repository.TourRepository;
import com.ssafy.yeogida.visit.domain.Visit;
import com.ssafy.yeogida.visit.dto.VisitModifyRequest;
import com.ssafy.yeogida.visit.dto.VisitRegistRequest;
import com.ssafy.yeogida.visit.dto.VisitResponse;
import com.ssafy.yeogida.visit.repository.VisitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitService {

	private final VisitRepository visitRepository;
	private final MemberRepository memberRepository;
	private final TourRepository tourRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public void registVisit(VisitRegistRequest visitRegistRequest) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(visitRegistRequest.getJwtToken()));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		Tour tour = tourRepository.findById(visitRegistRequest.getTourId())
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ERROR_MESSAGE));
		visitRepository.save(Visit.from(member, tour, visitRegistRequest.getUserScore()));
	}

	@Transactional(readOnly = true)
	public List<VisitResponse> getVisitList(String jwtToken) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(jwtToken));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		List<Visit> visitList = visitRepository.findAllByMember(member);
		return visitList.stream()
			.map(visit -> VisitResponse.from(visit))
			.collect(Collectors.toList());
	}

	public void deleteVisit(Long id, String jwtToken) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(jwtToken));
		Visit visit = visitRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_VISIT_ERROR_MESSAGE));
		if (visit.getMember().getId() != memberId) {
			new IllegalArgumentException(NOT_SAME_MEMBER_ERROR_MESSAGE);
		}
		visitRepository.delete(visit);
	}

	public void modifyVisit(VisitModifyRequest visitModifyRequest) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(visitModifyRequest.getJwtToken()));
		Visit visit = visitRepository.findById(visitModifyRequest.getId())
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_VISIT_ERROR_MESSAGE));
		if (visit.getMember().getId() != memberId) {
			new IllegalArgumentException(NOT_SAME_MEMBER_ERROR_MESSAGE);
		}
		visit.setScore(visitModifyRequest.getScore());
	}
}
