package com.ssafy.yeogida.tour.service;

import static com.ssafy.yeogida.member.MemberConstant.*;
import static com.ssafy.yeogida.tour.TourConstant.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.ssafy.yeogida.member.JwtTokenProvider;
import com.ssafy.yeogida.member.domain.Member;
import com.ssafy.yeogida.member.repository.MemberRepository;
import com.ssafy.yeogida.region.domain.Do;
import com.ssafy.yeogida.region.domain.Si;
import com.ssafy.yeogida.region.repository.DoRepository;
import com.ssafy.yeogida.region.repository.SiRepository;
import com.ssafy.yeogida.tour.domain.Tour;
import com.ssafy.yeogida.tour.dto.TourResponse;
import com.ssafy.yeogida.tour.repository.TourRepository;
import com.ssafy.yeogida.visit.domain.Visit;
import com.ssafy.yeogida.visit.repository.VisitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourService {

	private final TourRepository tourRepository;
	private final VisitRepository visitRepository;
	private final MemberRepository memberRepository;
	private final SiRepository siRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private Random random = new Random();

	public List<TourResponse> getTest() {
		List<Tour> tourList = tourRepository.findAll();
		return tourList.stream()
			.map(tour -> TourResponse.from(tour))
			.collect(Collectors.toList());
	}

	public TourResponse getTourByTourId(Long id) {
		return TourResponse.from(tourRepository
			.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ERROR_MESSAGE)));
	}

	public List<TourResponse> getTourListByDoId(Long doId, String jwtToken) throws ParseException {
		return getTourListByCore(getJsonArray("recombylogdo", doId, getVisitId(jwtToken).toString()), doId);
	}

	public List<TourResponse> getTourListBySiId(Long siId, String jwtToken) throws ParseException {
		return getTourListByCore(getJsonArray("recombylogsi", siId, getVisitId(jwtToken).toString()), siId);
	}

	public List<TourResponse> getTourListByKeywordsWithDo(Long doId, String keywords) throws ParseException {
		return getTourListByCore(getJsonArray("recombykeyworddo", doId, keywords), doId);
	}

	public List<TourResponse> getTourListByKeywordsWithSi(Long siId, String keywords) throws ParseException {
		return getTourListByCore(getJsonArray("recombykeywordsi", siId, keywords), siId);
	}

	public List<TourResponse> getTourListByKeywords(String keywords) throws ParseException {
		return getTourListByCore(getJsonArray("recombykeywords", null, keywords), null);
	}

	public List<TourResponse> getTourListByWeather(String weather) {
		return tourRepository.findAllByWeather(weather)
			.stream()
			.map(tour -> TourResponse.from(tour))
			.collect(Collectors.toList());
	}

	public List<TourResponse> getTourListByGenderAndAge(String jwtToken) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(jwtToken));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		Si si = siRepository.findById(249L)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		return tourRepository.findTop3BySi(si)
			.stream()
			.map(tour -> TourResponse.from(tour))
			.collect(Collectors.toList());
	}

	private String apiResponseToJson(String type, Long id, String keyword) {
		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
			.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
			.build();
		String path = type + "/{id}/" + keyword;
		WebClient webClient = WebClient.builder()
			.exchangeStrategies(exchangeStrategies)
			.baseUrl("http://j7b308.p.ssafy.io:8000/recomm/")
			.build();
		String path2 = type + "/" + keyword;
		if (id == null) {
			return webClient
				.get()
				.uri(uriBuilder -> uriBuilder.path(path2).build())
				.exchange()
				.block()
				.bodyToMono(String.class)
				.block();
		}
		return webClient
			.get()
			.uri(uriBuilder -> uriBuilder.path(path).build(id))
			.exchange()
			.block()
			.bodyToMono(String.class)
			.block();
	}

	private Long getVisitId(String jwtToken) {
		Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(jwtToken));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_MEMBER_ERROR_MESSAGE));
		Long visitId = 1L;
		try {
			Visit visit = visitRepository.findTop1ByMemberOrderByIdDesc(member)
				.orElseThrow(() -> new IllegalArgumentException());
			visitId = visit.getTour().getId();
		} catch (IllegalArgumentException e) {
			visitId = (long)(random.nextInt(19632) + 1);
		}
		return visitId;
	}

	private List<TourResponse> getTourListByCore(JSONArray jsonArray, Long idIsNull) {
		List<TourResponse> tourResponseList = new ArrayList<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			Long id;
			String str = jsonArray.get(i).toString();
			int length = str.length();
			if (idIsNull == null) {
				id = Long.parseLong(str) + 1L;
			} else {
				id = Long.parseLong(str.substring(1, length - 1)) + 1L;
			}
			Tour tour = tourRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ERROR_MESSAGE));
			tourResponseList.add(TourResponse.from(tour));
		}
		return tourResponseList;
	}

	private JSONArray getJsonArray(String path, Long id, String keywords) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		return (JSONArray)jsonParser.parse(apiResponseToJson(path, id, keywords));
	}

}
