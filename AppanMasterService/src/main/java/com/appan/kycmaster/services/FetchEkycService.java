package com.appan.kycmaster.services;

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
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.kyc.ekyc.model.EkycMasterModel;
import com.appan.kyc.ekyc.model.FetchEkycRequest;
import com.appan.kyc.ekyc.model.FetchEkycResponse;
import com.appan.kycmaster.entity.EkycMaster;
import com.appan.kycmaster.repositories.KycRepositories.EkycMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchEkycService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchEkycService.class);

	@Autowired
	private EkycMasterRepository ekycMasterRepository;

	public FetchEkycResponse fetch(FetchEkycRequest req, Integer pageNo, Integer pageSize) {
		logger.info("\r\n\r\n**************************** FETCH EKYC DETAILS *************************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<EkycMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));

				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<EkycMaster> pageResults = ekycMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No eKYC records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<EkycMasterModel> ekycData = pageResults.stream().map(this::convertToEkycModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", ekycData, pageDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<EkycMaster> root, FetchEkycRequest req) {
		List<Predicate> predicates = new ArrayList<>();

		if (req.getEkycName() != null && !req.getEkycName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("ekycName")), req.getEkycName().toLowerCase()));
		}
		if (req.getCode() != null && !req.getCode().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("code")), req.getCode().toLowerCase()));
		}
		if (req.getStatus() != null && !req.getStatus().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}
		if (req.getFromDate() != null && req.getToDate() != null) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());

				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				logger.error("Error parsing date filters", e);
			}
		}

		return predicates;
	}

	private EkycMasterModel convertToEkycModel(EkycMaster ekycMaster) {
		EkycMasterModel model = new EkycMasterModel();
		model.setId(ekycMaster.getId());
		model.setEkycName(ekycMaster.getEkycName());
		model.setCode(ekycMaster.getCode());
		model.setType(ekycMaster.getType());
		model.setApiName(ekycMaster.getApiName());
		model.setStatus(ekycMaster.getStatus());
		model.setCreatedBy(ekycMaster.getCreatedBy());
		model.setCreatedDt(ekycMaster.getCreatedDt());
		model.setModifyBy(ekycMaster.getModifyBy());
		model.setModifyDt(ekycMaster.getModifyDt());
		model.setAuthBy(ekycMaster.getAuthBy());
		model.setAuthDate(ekycMaster.getAuthDate());
		
		if (Strings.isNullOrEmpty(ekycMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (ekycMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (ekycMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (ekycMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(ekycMaster.getAuthStatus());
		}

		return model;
	}

	private FetchEkycResponse buildErrorResponse(boolean status, String message, String respCode,
			List<EkycMasterModel> data, PageDetails pageDetails) {
		FetchEkycResponse response = new FetchEkycResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchEkycResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL EKYC DATA *************************************");

		FetchEkycResponse response = new FetchEkycResponse();
		try {
			List<EkycMaster> allEkyc = ekycMasterRepository.findByStatus("Active");
			if (allEkyc.isEmpty()) {
				return buildErrorResponse(false, "No eKYC records found.", "01", null, null);
			}

			List<EkycMasterModel> ekycData = allEkyc.stream().map(this::convertToEkycModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", ekycData, null);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
