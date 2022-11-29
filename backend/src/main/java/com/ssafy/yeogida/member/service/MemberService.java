package com.ssafy.yeogida.member.service;

import static com.ssafy.yeogida.member.MemberConstant.*;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.yeogida.member.JwtTokenProvider;
import com.ssafy.yeogida.member.domain.EmailCode;
import com.ssafy.yeogida.member.domain.Member;
import com.ssafy.yeogida.member.dto.MailVo;
import com.ssafy.yeogida.member.dto.MemberJoinRequest;
import com.ssafy.yeogida.member.dto.MemberLoginRequest;
import com.ssafy.yeogida.member.dto.MemberModifyRequest;
import com.ssafy.yeogida.member.dto.MemberResponse;
import com.ssafy.yeogida.member.repository.EmailCodeRepository;
import com.ssafy.yeogida.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;
	private final EmailCodeRepository emailCodeRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String fromAddress;

	@Transactional(readOnly = true)
	public String login(MemberLoginRequest memberLoginRequest) {
		Member member = memberRepository.findMemberByEmail(memberLoginRequest.getEmail())
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		if (!passwordEncoder.matches(memberLoginRequest.getPw(), member.getPw())) {
			throw new IllegalArgumentException(MISMATCH_PW_ERROR_MESSAGE);
		}
		return jwtTokenProvider.createToken(member.getId());
	}

	public void join(MemberJoinRequest memberJoinRequest) {
		memberJoinRequest.pwEncode(passwordEncoder.encode(memberJoinRequest.getPw()));
		Member member = Member.from(memberJoinRequest);
		memberRepository.save(member);
	}

	public void changePw(MemberModifyRequest memberModifyRequest) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(memberModifyRequest.getJwtToken()));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		String encodePw = passwordEncoder.encode(memberModifyRequest.getPw());
		member.updatePw(encodePw);
	}

	public void deleteMember(String jwtToken) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(jwtToken));
		memberRepository.deleteById(memberId);
	}

	@Transactional(readOnly = true)
	public MemberResponse getMember(String jwtToken) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(jwtToken));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		return MemberResponse.from(member);
	}

	public void checkEmail(String userEmail) {
		String code = getCertifiedCode();
		String title = "여기다 이메일 인증 번호 안내 이메일입니다.";
		String message = "안녕하세요. 여기다 이메일 인증번호 안내 입니다." + "\n" +
			"회원님의 이메일 인증 번호는 아래와 같습니다." + "\n" +
			"회원 가입 페이지로 이동 후 이메일 인증을 진행해주세요." + "\n" +
			code;
		boolean duplicate = memberRepository.existsByEmail(userEmail);
		System.out.println(duplicate);
		if (duplicate == true) {
			throw new IllegalArgumentException(DUPLICATED_MAIL_ERROR_MESSAGE);
		}
		else {
			emailCodeRepository.deleteAllByEmail(userEmail);
			sendMail(createMail(title, message, userEmail));
			EmailCode emailCode = EmailCode.from(userEmail, code);
			emailCodeRepository.save(emailCode);
		}
	}

	public void checkEmailCode(String userEmail, String code) {
		EmailCode emailCode = emailCodeRepository.findByEmail(userEmail)
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_EMAIL_ERROR_MESSAGE));
		if (!emailCode.getCode().equals(code)) {
			throw new IllegalArgumentException(MISMATCH_CODE_ERROR_MESSAGE);
		}
	}

	public void sendPw(String email) {
		String code = getCertifiedCode();
		String title = "여기다 임시 비밀번호 안내 이메일입니다.";
		String message = "안녕하세요. 여기다 임시 비밀번호 안내 입니다." + "\n" +
			"회원님의 임시 비밀번호는 아래와 같습니다." + "\n" +
			"로그인 후 비밀번호 변경을 꼭 진행해 주세요." + "\n" +
			code;
		Member member = memberRepository.findMemberByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		sendMail(createMail(title, message, email));
		member.updatePw(passwordEncoder.encode(code));
	}

	private String getCertifiedCode() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();

		do {
			int num = random.nextInt(75) + 48;
			if (num <= 57 || num >= 65 && num <= 90 || num >= 97) {
				sb.append((char)num);
			}
		} while (sb.length() < 10);
		return sb.toString();
	}

	private MailVo createMail(String title, String message, String toAddress) {
		return MailVo.from(toAddress, title, message, fromAddress);
	}

	private void sendMail(MailVo mailVo) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(mailVo.getToAddress());
		mailMessage.setSubject(mailVo.getTitle());
		mailMessage.setText(mailVo.getMessage());
		mailMessage.setFrom(mailVo.getFromAddress());
		mailMessage.setReplyTo(mailVo.getFromAddress());
		javaMailSender.send(mailMessage);
	}
}
