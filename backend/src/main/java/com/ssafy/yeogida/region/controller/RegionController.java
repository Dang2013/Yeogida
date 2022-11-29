package com.ssafy.yeogida.region.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yeogida.region.dto.DoResponse;
import com.ssafy.yeogida.region.dto.SiResponse;
import com.ssafy.yeogida.region.service.DoService;
import com.ssafy.yeogida.region.service.SiService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {

	private final DoService doService;
	private final SiService siService;

	@GetMapping("/do")
	@ApiOperation(value = "광역시/도 데이터 조회", notes = "광역시/도 데이터 조회")
	public ResponseEntity<List<DoResponse>> getDoList() {
		return new ResponseEntity<>(doService.getDoList(), HttpStatus.OK);
	}

	@GetMapping("/si/{doId}")
	@ApiOperation(value = "시/군/구 데이터 조회", notes = "시/군/구 데이터 조회")
	public ResponseEntity<List<SiResponse>> getSiList(@PathVariable Long doId) {
		return new ResponseEntity<>(siService.getSiList(doId), HttpStatus.OK);
	}
}
