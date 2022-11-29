package com.ssafy.yeogida.visit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.yeogida.member.domain.Member;
import com.ssafy.yeogida.visit.domain.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {
	List<Visit> findAllByMember(Member member);

	Optional<Visit> findTop1ByMemberOrderByIdDesc(Member member);
}
