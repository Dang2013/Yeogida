package com.ssafy.yeogida.wish.service;

import static com.ssafy.yeogida.member.MemberConstant.*;
import static com.ssafy.yeogida.tour.TourConstant.*;
import static com.ssafy.yeogida.wish.WishConstant.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yeogida.member.JwtTokenProvider;
import com.ssafy.yeogida.member.domain.Member;
import com.ssafy.yeogida.member.repository.MemberRepository;
import com.ssafy.yeogida.tour.domain.Tour;
import com.ssafy.yeogida.tour.dto.TourResponse;
import com.ssafy.yeogida.tour.repository.TourRepository;
import com.ssafy.yeogida.wish.domain.Wish;
import com.ssafy.yeogida.wish.dto.WishRegistRequest;
import com.ssafy.yeogida.wish.repository.WishRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WishService {

	private final WishRepository wishRepository;
	private final MemberRepository memberRepository;
	private final TourRepository tourRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public void registWish(WishRegistRequest wishRegistRequest) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(wishRegistRequest.getJwtToken()));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		Tour tour = tourRepository.findById(wishRegistRequest.getTourId())
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ERROR_MESSAGE));
		if(wishRepository.existsByMemberAndTour(member, tour)) {
			throw new IllegalArgumentException(DUPLICATED_WISH_ERROR_MESSAGE);
		}
		wishRepository.save(Wish.from(member, tour));
	}

	public void deleteWish(Long id, String jwtToken) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(jwtToken));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		Tour tour = tourRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ERROR_MESSAGE));
		Wish wish = wishRepository.findByMemberAndTour(member, tour)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_WISH_ERROR_MESSAGE));
		if (wish.getMember().getId() != memberId) {
			new IllegalArgumentException(NOT_SAME_MEMBER_ERROR_MESSAGE);
		}
		wishRepository.delete(wish);
	}

	@Transactional(readOnly = true)
	public List<TourResponse> getWishList(String jwtToken) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(jwtToken));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		return wishRepository.findAllByMember(member)
			.stream()
			.map(wish -> TourResponse.from(wish.getTour()))
			.collect(Collectors.toList());
	}
}
