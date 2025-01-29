package com.appan.apimaster.services;

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
import com.appan.apimaster.models.FetchWhatsAppApiResponse;
import com.appan.apimaster.models.FetchWhatsappApiMaster;
import com.appan.apimaster.models.WhatsAppApiMasterModel;
import com.appan.apimaster.repositories.ApiRepositories.WhatsappApiMasterRepository;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.entity.WhatsappApiMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class WhatsappApiMasterFetchService {

	private static final Logger logger = LoggerFactory.getLogger(WhatsappApiMasterFetchService.class);

	@Autowired
	private WhatsappApiMasterRepository whatsappApiMasterRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public FetchWhatsAppApiResponse fetchWhatsappApi(FetchWhatsappApiMaster req, Integer pageNo, Integer pageSize) {
		logger.info("Fetching WhatsApp APIs with Filters");

		try {
			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<WhatsappApiMaster> specification = (root, query, cb) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(cb, root, req);
				} catch (ParseException e) {
					logger.error("Error parsing date filters", e);
					return null;
				}
				query.orderBy(cb.desc(root.get("createdDt")));
				return cb.and(predicates.toArray(new Predicate[0]));
			};

			Page<WhatsappApiMaster> pageResult = whatsappApiMasterRepository.findAll(specification, paging);

			if (pageResult.isEmpty()) {
				return buildErrorResponse(false, "No WhatsApp API records found", "01", null, null);
			}

			List<WhatsAppApiMasterModel> apiMasterModels = pageResult.stream()
					.map(this::convertToWhatsAppApiMasterModel).collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResult.getNumber() + 1);
			pageDetails.setTotalRecords(pageResult.getTotalElements());
			pageDetails.setNoOfPages(pageResult.getTotalPages());
			pageDetails.setPageSize(pageResult.getSize());

			return buildResponse(true, "SUCCESS", "00", apiMasterModels, pageDetails);

		} catch (Exception e) {
			logger.error("Exception while fetching WhatsApp APIs", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<WhatsappApiMaster> root,
			FetchWhatsappApiMaster req) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (req.getFromDate() != null && req.getToDate() != null) {
			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (req.getTenantName() != null && !req.getTenantName().isEmpty()) {
			predicates.add(cb.like(cb.lower(root.get("tenantName")), "%" + req.getTenantName().toLowerCase() + "%"));
		}

		if (req.getApiUrl() != null && !req.getApiUrl().isEmpty()) {
			predicates.add(cb.like(cb.lower(root.get("apiUrl")), "%" + req.getApiUrl().toLowerCase() + "%"));
		}

		if (req.getStatus() != null && !req.getStatus().equalsIgnoreCase("all")) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		return predicates;
	}

	private WhatsAppApiMasterModel convertToWhatsAppApiMasterModel(WhatsappApiMaster entity) {
		WhatsAppApiMasterModel model = new WhatsAppApiMasterModel();
		model.setId(entity.getId());
		model.setTenantName(entity.getTenantName());
		model.setApiUrl(entity.getApiUrl());
		model.setBalApiUrl(entity.getBalApiUrl());
		model.setStatus(entity.getStatus());
		model.setCreatedBy(entity.getCreatedBy());
		model.setCreatedDt(entity.getCreatedDt());
		model.setModifyBy(entity.getModifyBy());
		model.setModifyDt(entity.getModifyDt());
		model.setAuthBy(entity.getAuthBy());
		model.setAuthDate(entity.getAuthDate());
		if (Strings.isNullOrEmpty(entity.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (entity.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (entity.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (entity.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(entity.getAuthStatus());
		}

		return model;
	}

	private FetchWhatsAppApiResponse buildResponse(boolean status, String message, String respCode,
			List<WhatsAppApiMasterModel> data, PageDetails pageDetails) {
		FetchWhatsAppApiResponse response = new FetchWhatsAppApiResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	private FetchWhatsAppApiResponse buildErrorResponse(boolean status, String message, String respCode, Object data,
			PageDetails pageDetails) {
		return buildResponse(status, message, respCode, null, null);
	}

	public FetchWhatsAppApiResponse getAllWhatsappApis() {
		logger.info(
				"\r\n\r\n**************************** GET ALL WHATSAPP API DATA *************************************");

		try {
			List<WhatsappApiMaster> apiMasters = whatsappApiMasterRepository.findByStatus("Active");

			if (apiMasters.isEmpty()) {
				return buildResponse(false, "No WhatsApp API records found with authStatus = '1'", "01", null, null);
			}

			List<WhatsAppApiMasterModel> apiMasterModels = apiMasters.stream()
					.map(this::convertToWhatsAppApiMasterModel).collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", apiMasterModels, null);

		} catch (Exception e) {
			logger.error("Exception while fetching WhatsApp APIs", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

}
