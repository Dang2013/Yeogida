package com.ssafy.yeogida.wish.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.yeogida.member.domain.Member;
import com.ssafy.yeogida.tour.domain.Tour;
import com.ssafy.yeogida.wish.domain.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {
	List<Wish> findAllByMember(Member member);

	Optional<Wish> findByMemberAndTour(Member member, Tour tour);

	boolean existsByMemberAndTour(Member member, Tour tour);
}
