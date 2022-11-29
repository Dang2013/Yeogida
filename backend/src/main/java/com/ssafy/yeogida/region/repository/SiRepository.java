package com.ssafy.yeogida.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.yeogida.region.domain.Si;

public interface SiRepository extends JpaRepository<Si, Long> {
	List<Si> findAllByaDo_Id(Long doId);
}
