package com.ssafy.yeogida.member.dto;

import com.ssafy.yeogida.member.domain.Gender;
import com.ssafy.yeogida.member.domain.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {
	private String email;
	private String name;
	private Gender gender;
	private Long age;

	@Builder
	private MemberResponse(String email, String name, Gender gender, Long age) {
		this.email = email;
		this.name = name;
		this.gender = gender;
		this.age = age;
	}

	public static MemberResponse from(Member member) {
		return MemberResponse.builder()
			.email(member.getEmail())
			.name(member.getName())
			.gender(member.getGender())
			.age(member.getAge())
			.build();
	}
}
