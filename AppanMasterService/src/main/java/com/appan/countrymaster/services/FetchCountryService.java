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
import com.appan.countrymaster.country.models.CountryMasterModel;
import com.appan.countrymaster.country.models.FetchCountryRequest;
import com.appan.countrymaster.country.models.FetchCountryResponse;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.CountryMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CountryMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchCountryService {

	@Autowired
	private UserMasterRepository masterRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(FetchCountryService.class);

	@Autowired
	private CountryMasteRepository countryMasteRepository;

	public FetchCountryResponse fetch(FetchCountryRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH COUNTRY DETAILS *************************************");
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<CountryMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getCountryName(), req.getRegionName(),
							req.getRegex(), req.getIsdCode(), req.getNationality(), req.getFlag(), req.getStatus(),
							req.getFromDate(), req.getToDate());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (Exception e) {
					e.printStackTrace();
					Logger.info("EXCEPTION : " + e);
					return null;
				}
			};

			Page<CountryMaster> countryPage = countryMasteRepository.findAll(specification, paging);
			if (countryPage.isEmpty()) {
				return buildErrorResponse(false, "Country not found.", "01", null, null);
			}

			Logger.info("countryPage : " + countryPage);
			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(countryPage.getNumber() + 1);
			pageDetails.setTotalRecords(countryPage.getTotalElements());
			pageDetails.setNoOfPages(countryPage.getTotalPages());
			pageDetails.setPageSize(countryPage.getSize());

			List<CountryMasterModel> countryData = countryPage.stream().map(this::convertToCountryModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", countryData, pageDetails);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private CountryMasterModel convertToCountryModel(CountryMaster country) {
		CountryMasterModel model = new CountryMasterModel();
		model.setId(country.getId());
		model.setCountryName(country.getCountryName());
		model.setRegionName(country.getRegionName());
		model.setRegex(country.getRegex());
		model.setIsdCode(country.getIsdCode());
		model.setNationality(country.getNationality());
		model.setFlag(country.getFlag());
		model.setStatus(country.getStatus());
		model.setCreatedBy(country.getCreatedBy());
		model.setCreatedDt(country.getCreatedDt());
		model.setModifyBy(country.getModifyBy());
		model.setModifyDt(country.getModifyDt());
		model.setAuthBy(country.getAuthBy());
		model.setAuthDate(country.getAuthDate());

		if (Strings.isNullOrEmpty(country.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (country.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (country.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (country.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(country.getAuthStatus());
		}

		return model;
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<CountryMaster> root, String countryName,
			String regionName, String regex, String isdCode, String nationality, String flag, String authStatus,
			String fromDate, String toDate) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
			Date startDate = dateFormat.parse(fromDate);
			Date endDate = dateFormat.parse(toDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (!Strings.isNullOrEmpty(countryName) && !"all".equalsIgnoreCase(countryName)) {
			predicates.add(cb.like(cb.lower(root.get("countryName")), "%" + countryName.toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(regionName) && !"all".equalsIgnoreCase(regionName)) {
			predicates.add(cb.like(cb.lower(root.get("regionName")), "%" + regionName.toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(regex) && !"all".equalsIgnoreCase(regex)) {
			predicates.add(cb.like(cb.lower(root.get("regex")), "%" + regex.toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(isdCode) && !"all".equalsIgnoreCase(isdCode)) {
			predicates.add(cb.like(cb.lower(root.get("isdCode")), "%" + isdCode.toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(nationality) && !"all".equalsIgnoreCase(nationality)) {
			predicates.add(cb.like(cb.lower(root.get("nationality")), "%" + nationality.toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(flag) && !"all".equalsIgnoreCase(flag)) {
			predicates.add(cb.like(cb.lower(root.get("flag")), "%" + flag.toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(authStatus) && !"all".equalsIgnoreCase(authStatus)) {
			predicates.add(cb.like(cb.lower(root.get("authStatus")), "%" + authStatus.toLowerCase() + "%"));
		}

		return predicates;
	}

	private FetchCountryResponse buildErrorResponse(boolean status, String message, String respCode,
			List<CountryMasterModel> data, PageDetails pageDetails) {
		FetchCountryResponse response = new FetchCountryResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchCountryResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL COUNTRY CODE *************************************");
		FetchCountryResponse response = new FetchCountryResponse();
		try {

			List<CountryMaster> countryList = countryMasteRepository.findByStatus("Active");
			if (countryList.isEmpty()) {
				return buildErrorResponse(false, "Country not found.", "01", null, null);
			}

			List<CountryMasterModel> countryData = countryList.stream().map(this::convertToCountryModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", countryData, null);

		} catch (Exception e) {
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
