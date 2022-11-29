package com.ssafy.yeogida.tour.domain;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.ssafy.yeogida.region.domain.Do;
import com.ssafy.yeogida.region.domain.Si;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tour {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address;
	private String category;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "do_id")
	private Do aDo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "si_id")
	private Si si;
	private String withKeyword;
	private String tripKeyword;
	private String keywords;
	private String weather;

	public List<String> keywordsToList(String keywords) {
		return Arrays.asList(keywords.split(" "));
	}
}
