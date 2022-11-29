package com.ssafy.yeogida.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.yeogida.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findMemberByEmail(String email);

	boolean existsByEmail(String email);
}
