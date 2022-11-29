package com.ssafy.yeogida.tour.controller;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yeogida.tour.dto.TourResponse;
import com.ssafy.yeogida.tour.service.TourService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {

	private final TourService tourService;

	@GetMapping("/region/do")
	@ApiOperation(value = "관광지 조회", notes = "광역시/도 단위 관광지 조회")
	public ResponseEntity<List<TourResponse>> getTourListByDoId(
		@ApiParam(value = "id", required = true) @RequestParam String doId,
		@ApiParam(value = "jwt토큰", required = true) @RequestParam String jwtToken) throws ParseException {
		return new ResponseEntity<>(tourService.getTourListByDoId(Long.parseLong(doId), jwtToken), HttpStatus.OK);
	}

	@GetMapping("/region/si")
	@ApiOperation(value = "관광지 조회", notes = "시/군/구 단위 관광지 조회")
	public ResponseEntity<List<TourResponse>> getTourListBySiId(
		@ApiParam(value = "id", required = true) @RequestParam String siId,
		@ApiParam(value = "jwt토큰", required = true) @RequestParam String jwtToken) throws ParseException {
		return new ResponseEntity<>(tourService.getTourListBySiId(Long.parseLong(siId), jwtToken), HttpStatus.OK);
	}

	@GetMapping("/weather/{weather}")
	@ApiOperation(value = "날씨별 관광지 추천 조회", notes = "날씨별 관광지 추천 조회")
	public ResponseEntity<List<TourResponse>> getTourListByWeather(@PathVariable String weather) {
		return new ResponseEntity<>(tourService.getTourListByWeather(weather), HttpStatus.OK);
	}

	@GetMapping("/keywords/do")
	@ApiOperation(value = "키워드별 관광지 도단위 추천 조회", notes = "키워드별 관광지 도단위 추천 조회")
	public ResponseEntity<List<TourResponse>> getTourListByKeywordsWithDo(
		@ApiParam(value = "id", required = true) @RequestParam String doId,
		@ApiParam(value = "keywords", required = true) @RequestParam String keywords) throws ParseException {
		return new ResponseEntity<>(tourService.getTourListByKeywordsWithDo(Long.parseLong(doId), keywords),
			HttpStatus.OK);
	}

	@GetMapping("/keywords/si")
	@ApiOperation(value = "키워드별 관광지 시단위 추천 조회", notes = "키워드별 관광지 시단위 추천 조회")
	public ResponseEntity<List<TourResponse>> getTourListByKeywordsWithSi(
		@ApiParam(value = "id", required = true) @RequestParam String siId,
		@ApiParam(value = "keywords", required = true) @RequestParam String keywords) throws ParseException {
		return new ResponseEntity<>(tourService.getTourListByKeywordsWithSi(Long.parseLong(siId), keywords),
			HttpStatus.OK);
	}

	@GetMapping("/keywords")
	@ApiOperation(value = "키워드별 관광지 추천 조회", notes = "키워드별 관광지추천 조회")
	public ResponseEntity<List<TourResponse>> getTourListByKeywords(
		@ApiParam(value = "keywords", required = true) @RequestParam String keywords) throws ParseException {
		return new ResponseEntity<>(tourService.getTourListByKeywords(keywords),HttpStatus.OK);
	}

	@GetMapping("/gender")
	@ApiOperation(value = "성별/연령대별 관광지 추천 조회", notes = "성별/연령대별 관광지 추천 조회")
	public ResponseEntity<List<TourResponse>> getTourListByGenderAndAge(
		@ApiParam(value = "jwt토큰", required = true) @RequestParam String jwtToken) {
		return new ResponseEntity<>(tourService.getTourListByGenderAndAge(jwtToken), HttpStatus.OK);
	}

	@GetMapping("/{tourId}")
	@ApiOperation(value = "관광지 단건 조회", notes = "관광지 단건 조회")
	public ResponseEntity<TourResponse> getTourByTourId(@PathVariable Long tourId) {
		return new ResponseEntity<>(tourService.getTourByTourId(tourId), HttpStatus.OK);
	}
}
