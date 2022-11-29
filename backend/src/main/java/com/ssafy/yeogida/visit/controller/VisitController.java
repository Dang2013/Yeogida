package com.ssafy.yeogida.visit.controller;

import static com.ssafy.yeogida.visit.VisitConstant.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yeogida.visit.dto.VisitModifyRequest;
import com.ssafy.yeogida.visit.dto.VisitRegistRequest;
import com.ssafy.yeogida.visit.dto.VisitResponse;
import com.ssafy.yeogida.visit.service.VisitService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {

	private final VisitService visitService;

	@PostMapping
	@ApiOperation(value = "관광지 방문리스트 등록", notes = "관광지 방문리스트 등록")
	public ResponseEntity<String> registVisitList(@RequestBody VisitRegistRequest visitRegistRequest) {
		visitService.registVisit(visitRegistRequest);
		return new ResponseEntity<>(REGIST_SUCCESS_MESSAGE, HttpStatus.OK);
	}

	@GetMapping
	@ApiOperation(value = "관광지 방문리스트 조회", notes = "관광지 방문리스트 조회")
	public ResponseEntity<List<VisitResponse>> getVisitList(
		@ApiParam(value = "jwt토큰", required = true) @RequestParam String jwtToken) {
		return new ResponseEntity<>(visitService.getVisitList(jwtToken), HttpStatus.OK);
	}

	@DeleteMapping
	@ApiOperation(value = "관광지 방문 목록 삭제", notes = "관광지 방문 목록 삭제")
	public ResponseEntity<String> deleteVisit(@ApiParam(value = "id", required = true) @RequestParam String id,
		@ApiParam(value = "jwt토큰", required = true) @RequestParam String jwtToken) {
		visitService.deleteVisit(Long.parseLong(id), jwtToken);
		return new ResponseEntity<>(DELETE_VISIT_SUCCESS_MESSAGE, HttpStatus.OK);
	}

	@PutMapping
	@ApiOperation(value = "관광지 방문 점수 수정", notes = "관광지 방문 점수 수정")
	public ResponseEntity<String> modifyVisitScore(@RequestBody VisitModifyRequest visitModifyRequest) {
		visitService.modifyVisit(visitModifyRequest);
		return new ResponseEntity<>(MODIFY_VISIT_SUCCESS_MESSAGE, HttpStatus.OK);
	}
}
