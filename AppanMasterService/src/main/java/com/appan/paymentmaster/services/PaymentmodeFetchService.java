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
import com.appan.entity.PaymentModeMaster;
import com.appan.entity.UserMaster;
import com.appan.paymentmaster.model.FetchPaymentResponse;
import com.appan.paymentmaster.model.PaymentModeFetchRequest;
import com.appan.paymentmaster.model.PaymentModeMasterModel;
import com.appan.repositories.Repositories.PaymentModeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class PaymentmodeFetchService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentmodeFetchService.class);

	@Autowired
	private PaymentModeMasterRepository paymentModeRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public FetchPaymentResponse fetch(PaymentModeFetchRequest req, Integer pageNo, Integer pageSize) {
		logger.info("Fetching Payment Modes with Filters");

		try {
			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<PaymentModeMaster> specification = (root, query, criteriaBuilder) -> {
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

			Page<PaymentModeMaster> pageResult = paymentModeRepository.findAll(specification, paging);
			if (pageResult.isEmpty()) {
				return buildResponse(false, "Payment modes not found.", "01", null, null);
			}

			List<PaymentModeMasterModel> paymentModeModels = pageResult.stream()
					.map(this::convertToPaymentModeMasterModel).collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResult.getNumber() + 1);
			pageDetails.setTotalRecords(pageResult.getTotalElements());
			pageDetails.setNoOfPages(pageResult.getTotalPages());
			pageDetails.setPageSize(pageResult.getSize());

			return buildResponse(true, "SUCCESS", "00", paymentModeModels, pageDetails);

		} catch (Exception e) {
			logger.error("Exception while fetching payment modes", e);
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<PaymentModeMaster> root,
			PaymentModeFetchRequest req) throws ParseException {
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

		if (!Strings.isNullOrEmpty(req.getPaymentMode()) && !"all".equalsIgnoreCase(req.getPaymentMode())) {
			predicates.add(cb.like(cb.lower(root.get("paymentMode")), "%" + req.getPaymentMode().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(req.getUsername()) && !"all".equalsIgnoreCase(req.getUsername())) {
			predicates.add(cb.equal(cb.lower(root.get("createdBy")), req.getUsername().toLowerCase()));
		}

		return predicates;
	}

	private PaymentModeMasterModel convertToPaymentModeMasterModel(PaymentModeMaster paymentMode) {
		PaymentModeMasterModel model = new PaymentModeMasterModel();
		model.setId(paymentMode.getId());
		model.setPaymentMode(paymentMode.getPaymentMode());
		model.setImage(paymentMode.getImage());
		model.setStatus(paymentMode.getStatus());
		model.setCreatedBy(paymentMode.getCreatedBy());
		model.setCreatedDt(paymentMode.getCreatedDt());
		model.setModifyBy(paymentMode.getModifyBy());
		model.setModifyDt(paymentMode.getModifyDt());
		model.setAuthBy(paymentMode.getAuthBy());
		model.setAuthDate(paymentMode.getAuthDate());

		if (Strings.isNullOrEmpty(paymentMode.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (paymentMode.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (paymentMode.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (paymentMode.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(paymentMode.getAuthStatus());
		}

		return model;
	}

	private FetchPaymentResponse buildResponse(boolean status, String message, String respCode,
			List<PaymentModeMasterModel> data, PageDetails pageDetails) {
		FetchPaymentResponse response = new FetchPaymentResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchPaymentResponse getAllData() {
		logger.info("Fetching all payment modes");

		try {
			List<PaymentModeMaster> paymentModes = paymentModeRepository.findByStatus("Active");
			if (paymentModes.isEmpty()) {
				return buildResponse(false, "No payment modes found.", "01", null, null);
			}

			List<PaymentModeMasterModel> paymentModeModels = paymentModes.stream()
					.map(this::convertToPaymentModeMasterModel).collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", paymentModeModels, null);

		} catch (Exception e) {
			logger.error("Exception while fetching all payment modes", e);
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}
