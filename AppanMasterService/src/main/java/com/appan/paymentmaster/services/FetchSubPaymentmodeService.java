package com.appan.paymentmaster.services;

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
import com.appan.countrymaster.services.FetchPincodeService;
import com.appan.entity.SubPaymentMode;
import com.appan.entity.UserMaster;
import com.appan.paymentmaster.model.FetchSubPaymentResponse;
import com.appan.paymentmaster.model.SubPaymentModeFetchRequest;
import com.appan.paymentmaster.model.SubPaymentModel;
import com.appan.repositories.Repositories.SubPaymentModeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchSubPaymentmodeService {

	@Autowired
	private UserMasterRepository masterRepository;

	public static final Logger logger = LoggerFactory.getLogger(FetchPincodeService.class);

	@Autowired
	private SubPaymentModeRepository subPaymentModeRepository;

	public FetchSubPaymentResponse fetch(SubPaymentModeFetchRequest req, Integer pageNo, Integer pageSize) {
		logger.info(
				"\r\n\r\n**************************** FETCH SUB PAYMENT MODE DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<SubPaymentMode> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					logger.error("Error parsing date filters", e);
					return null;
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<SubPaymentMode> pageResult = subPaymentModeRepository.findAll(specification, paging);
			if (pageResult.isEmpty()) {
				return buildResponse(false, "Sub Payment details not found.", "01", null, null);
			}

			List<SubPaymentModel> subPaymentModels = pageResult.stream().map(this::convertToSubPaymentModel)
					.collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResult.getNumber() + 1);
			pageDetails.setTotalRecords(pageResult.getTotalElements());
			pageDetails.setNoOfPages(pageResult.getTotalPages());
			pageDetails.setPageSize(pageResult.getSize());

			return buildResponse(true, "SUCCESS", "00", subPaymentModels, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<SubPaymentMode> root,
			SubPaymentModeFetchRequest req) throws ParseException {

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

		if (req.getSubPaymentMode() != null && !req.getSubPaymentMode().isEmpty()) {
			predicates.add(
					cb.like(cb.lower(root.get("subPaymentMode")), "%" + req.getSubPaymentMode().toLowerCase() + "%"));
		}

		if (req.getPaymentMode() != null && !req.getPaymentMode().isEmpty()) {
			predicates.add(cb.like(cb.lower(root.get("paymentMode")), "%" + req.getPaymentMode().toLowerCase() + "%"));
		}

		if (req.getStatus() != null && !req.getStatus().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		return predicates;
	}

	private SubPaymentModel convertToSubPaymentModel(SubPaymentMode entity) {
		SubPaymentModel model = new SubPaymentModel();
		model.setId(entity.getId());
		model.setSubPaymentMode(entity.getSubPaymentMode());
		model.setPaymentMode(entity.getPaymentMode());
		model.setImage(entity.getImage());
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

	private FetchSubPaymentResponse buildResponse(boolean status, String message, String respCode,
			List<SubPaymentModel> data, PageDetails pageDetails) {
		FetchSubPaymentResponse response = new FetchSubPaymentResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchSubPaymentResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** GET ALL SUB PAYMENT DETAILS *************************************");
		FetchSubPaymentResponse response = new FetchSubPaymentResponse();
		try {
			List<SubPaymentMode> subPaymentList = subPaymentModeRepository.findByStatus("Active");
			if (subPaymentList.isEmpty()) {
				return buildResponse(false, "Sub payment details not found.", "01", null, null);
			}

			List<SubPaymentModel> subPaymentModels = subPaymentList.stream().map(this::convertToSubPaymentModel)
					.collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", subPaymentModels, null);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
