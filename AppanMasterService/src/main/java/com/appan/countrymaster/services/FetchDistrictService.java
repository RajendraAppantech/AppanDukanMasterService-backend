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
import com.appan.countrymaster.district.models.DistrictMasterModel;
import com.appan.countrymaster.district.models.FetchDistrictRequest;
import com.appan.countrymaster.district.models.FetchDistrictResponse;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.DistrictMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.DistrictMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchDistrictService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchDistrictService.class);

	@Autowired
	private DistrictMasteRepository districtMasteRepository;

	public FetchDistrictResponse fetch(FetchDistrictRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("Fetching district details with filters");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<DistrictMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = null;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<DistrictMaster> pageResults = districtMasteRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "District not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<DistrictMasterModel> districtData = pageResults.stream().map(this::convertToDistrictModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", districtData, pageDetails);

		} catch (Exception e) {
			Logger.error("EXCEPTION : ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<DistrictMaster> root, FetchDistrictRequest req)
			throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (!Strings.isNullOrEmpty(req.getDistrictName()) && !"all".equalsIgnoreCase(req.getDistrictName())) {
			predicates
					.add(cb.like(cb.lower(root.get("districtName")), "%" + req.getDistrictName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getStateName()) && !"all".equalsIgnoreCase(req.getStateName())) {
			predicates.add(cb.like(cb.lower(root.get("stateName")), "%" + req.getStateName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.equal(root.get("authStatus"), req.getStatus()));
		}

		return predicates;
	}

	private DistrictMasterModel convertToDistrictModel(DistrictMaster districtMaster) {
		DistrictMasterModel model = new DistrictMasterModel();
		model.setDistrictId(districtMaster.getDistrictId());
		model.setDistrictName(districtMaster.getDistrictName());
		model.setStateName(districtMaster.getStateName());
		model.setStatus(districtMaster.getStatus());
		model.setCreatedBy(districtMaster.getCreatedBy());
		model.setCreatedDt(districtMaster.getCreatedDt());
		model.setModifyBy(districtMaster.getModifyBy());
		model.setModifyDt(districtMaster.getModifyDt());
		model.setAuthBy(districtMaster.getAuthBy());
		model.setAuthDate(districtMaster.getAuthDate());
		if (Strings.isNullOrEmpty(districtMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (districtMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (districtMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (districtMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(districtMaster.getAuthStatus());
		}
		return model;
	}

	private FetchDistrictResponse buildErrorResponse(boolean status, String message, String respCode,
			List<DistrictMasterModel> data, PageDetails pageDetails) {
		FetchDistrictResponse response = new FetchDistrictResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchDistrictResponse getAllData() {
		Logger.info("Fetching all district details");

		try {
			List<DistrictMaster> allDistricts = districtMasteRepository.findByStatus("Active");
			if (allDistricts.isEmpty()) {
				return buildErrorResponse(false, "No districts found.", "01", null, null);
			}

			List<DistrictMasterModel> districtData = allDistricts.stream().map(this::convertToDistrictModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", districtData, null);

		} catch (Exception e) {
			Logger.error("EXCEPTION : ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}
