package com.ssafy.yeogida.region.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.yeogida.region.domain.Do;
import com.ssafy.yeogida.region.dto.DoResponse;
import com.ssafy.yeogida.region.repository.DoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoService {

	private final DoRepository doRepository;

	public List<DoResponse> getDoList() {
		List<Do> doList = doRepository.findAll();
		return doList.stream()
			.map(aDo -> DoResponse.from(aDo))
			.collect(Collectors.toList());
	}

}
