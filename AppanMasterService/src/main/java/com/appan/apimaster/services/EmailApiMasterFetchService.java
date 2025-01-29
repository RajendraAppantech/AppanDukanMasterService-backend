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
import com.appan.apimaster.models.FetchEmailApiMaster;
import com.appan.apimaster.models.FetchEmailApiMasterModel;
import com.appan.apimaster.models.FetchEmailApiResponse;
import com.appan.apimaster.repositories.ApiRepositories.EmailApiMasterRepository;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.EmailApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class EmailApiMasterFetchService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(EmailApiMasterFetchService.class);

	@Autowired
	private EmailApiMasterRepository emailApiMasterRepository;

	public FetchEmailApiResponse fetchEmailApi(FetchEmailApiMaster req, Integer pageNo, Integer pageSize) {
		Logger.info(
				"\r\n\r\n**************************** FETCH EMAIL API MASTER DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<EmailApiMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<EmailApiMaster> pageResults = emailApiMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Email API Master data not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<FetchEmailApiMasterModel> data = pageResults.stream().map(this::convertToFetchEmailApiMasterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", data, pageDetails);
		} catch (Exception e) {
			Logger.error("Exception: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<EmailApiMaster> root, FetchEmailApiMaster req) {
		List<Predicate> predicates = new ArrayList<>();

		if (req.getHostName() != null && !req.getHostName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("hostName")), req.getHostName().toLowerCase()));
		}
		if (req.getEmailAddress() != null && !req.getEmailAddress().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("emailAddress")), req.getEmailAddress().toLowerCase()));
		}

		if (req.getFromDate() != null && !req.getFromDate().isEmpty() && req.getToDate() != null
				&& !req.getToDate().isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date fromDate = dateFormat.parse(req.getFromDate());
				Date toDate = dateFormat.parse(req.getToDate());

				// Add an extra day to the toDate
				Calendar cal = Calendar.getInstance();
				cal.setTime(toDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				toDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), fromDate, toDate));
			} catch (ParseException e) {
				Logger.error("Error parsing date range: ", e);
			}
		}

		return predicates;
	}

	private FetchEmailApiMasterModel convertToFetchEmailApiMasterModel(EmailApiMaster entity) {
		FetchEmailApiMasterModel model = new FetchEmailApiMasterModel();
		model.setId(entity.getId());
		model.setHostName(entity.getHostName());
		model.setEmailAddress(entity.getEmailAddress());
		model.setCc(entity.getCc());
		model.setBcc(entity.getBcc());
		model.setPort(entity.getPort());
		model.setIsMailActive(entity.getIsMailActive());
		model.setMailSsl(entity.getMailSsl());
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

	private FetchEmailApiResponse buildErrorResponse(boolean status, String message, String respCode,
			List<FetchEmailApiMasterModel> data, PageDetails pageDetails) {
		FetchEmailApiResponse response = new FetchEmailApiResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchEmailApiResponse getAllData() {
		Logger.info(
				"\r\n\r\n**************************** GET ALL EMAIL API MASTER DETAILS *************************************");

		try {
			List<EmailApiMaster> allData = emailApiMasterRepository.findAll();
			if (allData.isEmpty()) {
				return buildErrorResponse(false, "No data found.", "01", null, null);
			}

			List<FetchEmailApiMasterModel> data = allData.stream().map(this::convertToFetchEmailApiMasterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", data, null);
		} catch (Exception e) {
			Logger.error("Exception: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}
