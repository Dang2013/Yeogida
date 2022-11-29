package com.ssafy.yeogida.wish.controller;

import static com.ssafy.yeogida.wish.WishConstant.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yeogida.tour.dto.TourResponse;
import com.ssafy.yeogida.wish.dto.WishRegistRequest;
import com.ssafy.yeogida.wish.service.WishService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/wish")
@RequiredArgsConstructor
public class WishController {

	private final WishService wishService;

	@PostMapping
	@ApiOperation(value = "관광지 찜리스트 등록", notes = "관광지 찜리스트 등록")
	public ResponseEntity<String> registWishList(@RequestBody WishRegistRequest wishRegistRequest) {
		wishService.registWish(wishRegistRequest);
		return new ResponseEntity<>(REGIST_SUCCESS_MESSAGE, HttpStatus.OK);
	}

	@DeleteMapping
	@ApiOperation(value = "찜리스트 삭제", notes = "찜리스트 삭제")
	public ResponseEntity<String> deleteWishList(@ApiParam(value = "id", required = true) @RequestParam String id,
		@ApiParam(value = "jwt토큰", required = true) @RequestParam String jwtToken) {
		wishService.deleteWish(Long.parseLong(id), jwtToken);
		return new ResponseEntity<>(DELETE_SUCCESS_MESSAGE, HttpStatus.OK);
	}

	@GetMapping
	@ApiOperation(value = "찜리스트 조회", notes = "찜리스트 조회")
	public ResponseEntity<List<TourResponse>> getWishList(
		@ApiParam(value = "jwt토큰", required = true) @RequestParam String jwtToken) {
		return new ResponseEntity<>(wishService.getWishList(jwtToken), HttpStatus.OK);
	}
}
