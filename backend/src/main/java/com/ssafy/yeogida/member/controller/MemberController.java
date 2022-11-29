package com.ssafy.yeogida.member.controller;

import static com.ssafy.yeogida.member.MemberConstant.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yeogida.member.dto.MemberJoinRequest;
import com.ssafy.yeogida.member.dto.MemberLoginRequest;
import com.ssafy.yeogida.member.dto.MemberModifyRequest;
import com.ssafy.yeogida.member.dto.MemberResponse;
import com.ssafy.yeogida.member.service.MemberService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes = "로그인 후 jwt토큰 반환")
	public ResponseEntity<String> login(@RequestBody MemberLoginRequest memberLoginRequest) {
		return new ResponseEntity<>(memberService.login(memberLoginRequest), HttpStatus.OK);
	}

	@PostMapping("/join")
	@ApiOperation(value = "회원가입", notes = "회원가입(id,pw,name,gender,age)")
	public ResponseEntity<String> join(@RequestBody MemberJoinRequest memberJoinRequest) {
		memberService.join(memberJoinRequest);
		return new ResponseEntity<>(JOIN_SUCCESS_RESPONSE_MESSAGE, HttpStatus.OK);
	}

	@PutMapping
	@ApiOperation(value = "비밀번호 변경", notes = "jwt토큰으로 회원 확인 후 비밀번호 변경")
	public ResponseEntity<String> changePw(@RequestBody MemberModifyRequest memberModifyRequest) {
		memberService.changePw(memberModifyRequest);
		return new ResponseEntity<>(MODIFY_SUCCESS_RESPONSE_MESSAGE, HttpStatus.OK);
	}

	@DeleteMapping
	@ApiOperation(value = "회원 탈퇴", notes = "jwt토큰으로 회원 확인 후 회원 탈퇴")
	public ResponseEntity<String> deleteMember(
		@ApiParam(value = "jwt토큰", required = true) @RequestParam String jwtToken) {
		memberService.deleteMember(jwtToken);
		return new ResponseEntity<>(DELETE_SUCCESS_RESPONSE_MESSAGE, HttpStatus.OK);
	}

	@GetMapping
	@ApiOperation(value = "회원 정보 조회", notes = "jwtToken으로 아이디 조회 후 회원 정보 조회")
	public ResponseEntity<MemberResponse> getMember(
		@ApiParam(value = "jwt토큰", required = true) @RequestParam String jwtToken) {
		return new ResponseEntity<>(memberService.getMember(jwtToken), HttpStatus.OK);
	}

	@GetMapping("/checkEmail")
	@ApiOperation(value = "이메일 중복 체크 & 이메일 인증", notes = "이메일 중복체크 후 통과한다면 이메일 인증번호 발송")
	public ResponseEntity<String> checkEmail(
		@ApiParam(value = "사용자 이메일", required = true) @RequestParam String email) {
		memberService.checkEmail(email);
		return new ResponseEntity<>(EMAIL_SEND_SUCCESS_MESSAGE, HttpStatus.OK);
	}

	@GetMapping("/cehckEmailCode")
	@ApiOperation(value = "이메일 인증번호 확인", notes = "이메일 인증번호 확인")
	public ResponseEntity<String> checkEmailCode(
		@ApiParam(value = "사용자 이메일", required = true) @RequestParam String email,
		@ApiParam(value = "이메일 인증번호", required = true) @RequestParam String code) {
		memberService.checkEmailCode(email, code);
		return new ResponseEntity<>(EMAIL_CERTIFICATION_SUCCESS_MESSAGE, HttpStatus.OK);
	}

	@GetMapping("/sendPw")
	@ApiOperation(value = "임시 비밀번호 발송", notes = "임시 비밀번호 발송")
	public ResponseEntity<String> sendPw(
		@ApiParam(value = "사용자 이메일", required = true) @RequestParam String email) {
		memberService.sendPw(email);
		return new ResponseEntity<>(SEND_PW_SUCCESS_MESSAGE, HttpStatus.OK);
	}

}
