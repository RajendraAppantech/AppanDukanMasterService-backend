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
import com.appan.entity.EmailTemplateMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.EmailTemplateFetchRequest;
import com.appan.notification.models.EmailTemplateFetchResponse;
import com.appan.repositories.Repositories.EmailTemplateMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class EmailTemplateFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private EmailTemplateMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(EmailTemplateFetchService.class);

	public EmailTemplateFetchResponse fetch(EmailTemplateFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<EmailTemplateMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getEmailType(),
						req.getSubject(), req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<EmailTemplateMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "EmailTemplate not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<EmailTemplateMaster> EmailTemplateData = pageResults.stream().map(this::convertToEmailTemplateModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", EmailTemplateData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<EmailTemplateMaster> root, String emailType,
			String subject, String status, String fromDate, String toDate) {
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

		if (!Strings.isNullOrEmpty(emailType) && !"all".equalsIgnoreCase(emailType)) {
			predicates.add(cb.equal(cb.lower(root.get("emailType")), emailType.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(subject) && !"all".equalsIgnoreCase(subject)) {
			predicates.add(cb.equal(cb.lower(root.get("subject")), subject.toLowerCase()));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private EmailTemplateMaster convertToEmailTemplateModel(EmailTemplateMaster EmailTemplateMaster) {
		EmailTemplateMaster model = EmailTemplateMaster;
		if (Strings.isNullOrEmpty(EmailTemplateMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (EmailTemplateMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (EmailTemplateMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (EmailTemplateMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(EmailTemplateMaster.getAuthStatus());
		}
		return model;
	}

	private EmailTemplateFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<EmailTemplateMaster> data, PageDetails pageDetails) {
		EmailTemplateFetchResponse response = new EmailTemplateFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public EmailTemplateFetchResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** GET ALL EmailTemplate  *************************************");

		EmailTemplateFetchResponse response = new EmailTemplateFetchResponse();
		try {
			List<EmailTemplateMaster> allEmailTemplate = repository.findByAuthStatus("1");
			if (allEmailTemplate.isEmpty()) {
				return buildErrorResponse(false, "No EmailTemplate found.", "01", null, null);
			}

			List<EmailTemplateMaster> EmailTemplateData = allEmailTemplate.stream()
					.map(this::convertToEmailTemplateModel).collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", EmailTemplateData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
