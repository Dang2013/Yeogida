package com.ssafy.yeogida.member.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailVo {
	private String toAddress;
	private String title;
	private String message;
	private String fromAddress;

	@Builder
	private MailVo(String toAddress, String title, String message, String fromAddress) {
		this.toAddress = toAddress;
		this.title = title;
		this.message = message;
		this.fromAddress = fromAddress;
	}

	public static MailVo from(String toAddress, String title, String message, String fromAddress) {
		return MailVo.builder()
			.toAddress(toAddress)
			.title(title)
			.message(message)
			.fromAddress(fromAddress)
			.build();
	}
}
