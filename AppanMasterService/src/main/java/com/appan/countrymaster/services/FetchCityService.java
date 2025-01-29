package com.appan.countrymaster.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.city.models.CityMasterModel;
import com.appan.countrymaster.city.models.FetchCityRequest;
import com.appan.countrymaster.city.models.FetchCityResponse;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.CityMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CityMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchCityService {

	@Autowired
	private UserMasterRepository masterRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(FetchCityService.class);

	@Autowired
	private CityMasteRepository cityMasteRepository;

	public FetchCityResponse fetch(FetchCityRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH CITY DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<CityMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getCityName(),
						req.getDistrictName(), req.getStateName(), req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<CityMaster> pageResults = cityMasteRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "City not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<CityMasterModel> cityData = pageResults.stream().map(this::convertToCityModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", cityData, pageDetails);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<CityMaster> root, String cityName,
			String districtName, String stateName, String authStatus, String fromDate, String toDate) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
			try {
				Date startDate = dateFormat.parse(fromDate);
				Date endDate = dateFormat.parse(toDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				Logger.error("Error parsing date filters", e);
			}
		}

		if (!Strings.isNullOrEmpty(cityName) && !"all".equalsIgnoreCase(cityName)) {
			predicates.add(cb.equal(cb.lower(root.get("cityName")), cityName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(districtName) && !"all".equalsIgnoreCase(districtName)) {
			predicates.add(cb.equal(cb.lower(root.get("districtName")), districtName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(stateName) && !"all".equalsIgnoreCase(stateName)) {
			predicates.add(cb.equal(cb.lower(root.get("stateName")), stateName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(authStatus) && !"all".equalsIgnoreCase(authStatus)) {
			predicates.add(cb.equal(cb.lower(root.get("authStatus")), authStatus.toLowerCase()));
		}

		return predicates;
	}

	private CityMasterModel convertToCityModel(CityMaster cityMaster) {
		CityMasterModel model = new CityMasterModel();
		model.setCityId(cityMaster.getCityId());
		model.setCityName(cityMaster.getCityName());
		model.setDistrictName(cityMaster.getDistrictName());
		model.setStateName(cityMaster.getStateName());
		model.setCreatedBy(cityMaster.getCreatedBy());
		model.setCreatedDt(cityMaster.getCreatedDt());
		model.setModifyBy(cityMaster.getModifyBy());
		model.setModifyDt(cityMaster.getModifyDt());
		model.setAuthBy(cityMaster.getAuthBy());
		model.setAuthDate(cityMaster.getAuthDate());
		model.setStatus(cityMaster.getStatus());

		if (Strings.isNullOrEmpty(cityMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (cityMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (cityMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (cityMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(cityMaster.getAuthStatus());
		}

		return model;
	}

	private FetchCityResponse buildErrorResponse(boolean status, String message, String respCode,
			List<CityMasterModel> data, PageDetails pageDetails) {
		FetchCityResponse response = new FetchCityResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchCityResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL CITY CODE *************************************");

		FetchCityResponse response = new FetchCityResponse();
		try {
			List<CityMaster> allCities = cityMasteRepository.findByStatus("Active");
			if (allCities.isEmpty()) {
				return buildErrorResponse(false, "No cities found.", "01", null, null);
			}

			List<CityMasterModel> cityData = allCities.stream().map(this::convertToCityModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", cityData, null);

		} catch (Exception e) {
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
