package com.ssafy.yeogida.member.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ssafy.yeogida.member.dto.MemberJoinRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String pw;
	private String name;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	private Long age;

	@Builder
	private Member(String email, String pw, String name, Gender gender, Long age) {
		this.email = email;
		this.pw = pw;
		this.name = name;
		this.gender = gender;
		this.age = age;
	}

	public static Member from(MemberJoinRequest memberJoinRequest) {
		return Member.builder()
			.email(memberJoinRequest.getEmail())
			.pw(memberJoinRequest.getPw())
			.name(memberJoinRequest.getName())
			.gender(memberJoinRequest.getGender())
			.age(memberJoinRequest.getAge())
			.build();
	}

	public void updatePw(String pw) {
		this.pw = pw;
	}

}
