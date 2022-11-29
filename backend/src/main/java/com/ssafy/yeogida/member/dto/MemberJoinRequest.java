package com.ssafy.yeogida.member.dto;

import com.ssafy.yeogida.member.domain.Gender;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberJoinRequest {
	private String email;
	private String pw;
	private String name;
	private Gender gender;
	private Long age;

	public void pwEncode(String encodePw) {
		this.pw = encodePw;
	}
}
