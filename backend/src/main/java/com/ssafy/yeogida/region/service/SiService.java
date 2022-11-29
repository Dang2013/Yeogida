package com.ssafy.yeogida.region.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.yeogida.region.domain.Si;
import com.ssafy.yeogida.region.dto.SiResponse;
import com.ssafy.yeogida.region.repository.SiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SiService {

	private final SiRepository siRepository;

	public List<SiResponse> getSiList(Long doId) {
		List<Si> siList = siRepository.findAllByaDo_Id(doId);
		return siList.stream()
			.map(si -> SiResponse.from(si))
			.collect(Collectors.toList());
	}
}
