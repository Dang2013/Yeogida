package com.ssafy.yeogida.tour.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.yeogida.region.domain.Do;
import com.ssafy.yeogida.region.domain.Si;
import com.ssafy.yeogida.tour.domain.Tour;

public interface TourRepository extends JpaRepository<Tour, Long> {
	List<Tour> findAllByWeather(String weather);
	List<Tour> findTop3BySi(Si si);
}
