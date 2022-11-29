package com.ssafy.yeogida.member.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberLoginRequest {
	private String email;
	private String pw;
}
