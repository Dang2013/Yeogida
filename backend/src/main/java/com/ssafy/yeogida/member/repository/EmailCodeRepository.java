package com.ssafy.yeogida.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.yeogida.member.domain.EmailCode;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {
	void deleteAllByEmail(String email);

	Optional<EmailCode> findByEmail(String email);
}
