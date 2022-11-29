package com.ssafy.yeogida.member.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberModifyRequest {
	private String jwtToken;
	private String pw;
}
