package com.ssafy.yeogida.member.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String code;

	@Builder
	private EmailCode(String email, String code) {
		this.email = email;
		this.code = code;
	}

	public static EmailCode from(String email, String code) {
		return EmailCode.builder()
			.email(email)
			.code(code)
			.build();
	}

}
