package com.appan.notification.services;

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
import com.appan.entity.SmsTemplateMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.SmsTemplateFetchRequest;
import com.appan.notification.models.SmsTemplateFetchResponse;
import com.appan.repositories.Repositories.SmsTemplateMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class SmsTemplateFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SmsTemplateMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(SmsTemplateFetchService.class);

	public SmsTemplateFetchResponse fetch(SmsTemplateFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<SmsTemplateMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getSmsType(),
						req.getTemplateId(), req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<SmsTemplateMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "SmsTemplate not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<SmsTemplateMaster> SmsTemplateData = pageResults.stream().map(this::convertToSmsTemplateModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", SmsTemplateData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<SmsTemplateMaster> root, String smsType,
			String templateId, String status, String fromDate, String toDate) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (fromDate != null && toDate != null) {
			try {
				Date startDate = dateFormat.parse(fromDate);
				Date endDate = dateFormat.parse(toDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();
				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				logger.error("Error parsing date filters", e);
			}
		}

		if (!Strings.isNullOrEmpty(smsType) && !"all".equalsIgnoreCase(smsType)) {
			predicates.add(cb.equal(cb.lower(root.get("smsType")), smsType.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(templateId) && !"all".equalsIgnoreCase(templateId)) {
			predicates.add(cb.equal(cb.lower(root.get("templateId")), templateId.toLowerCase()));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private SmsTemplateMaster convertToSmsTemplateModel(SmsTemplateMaster SmsTemplateMaster) {
		SmsTemplateMaster model = SmsTemplateMaster;
		if (Strings.isNullOrEmpty(SmsTemplateMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (SmsTemplateMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (SmsTemplateMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (SmsTemplateMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(SmsTemplateMaster.getAuthStatus());
		}
		return model;
	}

	private SmsTemplateFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<SmsTemplateMaster> data, PageDetails pageDetails) {
		SmsTemplateFetchResponse response = new SmsTemplateFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public SmsTemplateFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL SmsTemplate  *************************************");

		SmsTemplateFetchResponse response = new SmsTemplateFetchResponse();
		try {
			List<SmsTemplateMaster> allSmsTemplate = repository.findByAuthStatus("1");
			if (allSmsTemplate.isEmpty()) {
				return buildErrorResponse(false, "No SmsTemplate found.", "01", null, null);
			}

			List<SmsTemplateMaster> SmsTemplateData = allSmsTemplate.stream().map(this::convertToSmsTemplateModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", SmsTemplateData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
