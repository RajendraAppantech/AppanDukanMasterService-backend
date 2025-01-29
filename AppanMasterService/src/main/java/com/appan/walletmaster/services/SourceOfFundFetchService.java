package com.appan.walletmaster.services;

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

import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.SourceOfFund;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.SourceOfFundRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.walletmaster.model.SourceOfFundFetchRequest;
import com.appan.walletmaster.model.SourceOfFundFetchResponse;
import com.appan.walletmaster.model.SourseOfFundModel;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class SourceOfFundFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SourceOfFundRepository sourceOfFundRepository;

	private static final Logger logger = LoggerFactory.getLogger(SourceOfFundFetchService.class);

	public SourceOfFundFetchResponse fetchSourceOfFund(SourceOfFundFetchRequest req, Integer pageNo, Integer pageSize) {
		logger.info(
				"\r\n\r\n**************************** FETCH SOURCE OF FUND DETAILS *************************************");

		try {
			UserMaster userMaster = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (userMaster == null) {
				return buildErrorResponse(false, "Invalid Username", "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<SourceOfFund> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<SourceOfFund> pageResults = sourceOfFundRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No Source of Fund found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<SourseOfFundModel> sourceOfFundData = pageResults.stream().map(this::convertToSourseOfFundModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", sourceOfFundData, pageDetails);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION: ", e);
			return buildErrorResponse(false, "Exception occurred", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<SourceOfFund> root,
			SourceOfFundFetchRequest req) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
			try {
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());

				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();

				predicates.add(criteriaBuilder.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				logger.error("Error parsing date filters", e);
			}
		}

		if (!Strings.isNullOrEmpty(req.getSourceName()) && !"all".equalsIgnoreCase(req.getSourceName())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("sourceName")),
					req.getSourceName().toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(req.getCategoryName()) && !"all".equalsIgnoreCase(req.getCategoryName())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("categoryName")),
					req.getCategoryName().toLowerCase()));
		}
		if (req.getRank() != null) {
			predicates.add(criteriaBuilder.equal(root.get("rank"), req.getRank()));
		}
		if (!Strings.isNullOrEmpty(req.getCode())) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("code")), req.getCode().toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(
					criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(
					criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(req.getDescription()) && !"all".equalsIgnoreCase(req.getDescription())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
					"%" + req.getDescription().toLowerCase() + "%"));
		}

		return predicates;
	}

	private SourseOfFundModel convertToSourseOfFundModel(SourceOfFund sourceOfFund) {
		SourseOfFundModel model = new SourseOfFundModel();
		model.setId(sourceOfFund.getId());
		model.setSourceName(sourceOfFund.getSourceName());
		model.setCategoryName(sourceOfFund.getCategoryName());
		model.setRank(sourceOfFund.getRank());
		model.setCode(sourceOfFund.getCode());
		model.setDescription(sourceOfFund.getDescription());
		model.setStatus(sourceOfFund.getStatus());
		model.setCreatedBy(sourceOfFund.getCreatedBy());
		model.setCreatedDt(sourceOfFund.getCreatedDt());
		model.setModifyBy(sourceOfFund.getModifyBy());
		model.setModifyDt(sourceOfFund.getModifyDt());
		model.setAuthBy(sourceOfFund.getAuthBy());
		model.setAuthDate(sourceOfFund.getAuthDate());
		if (Strings.isNullOrEmpty(sourceOfFund.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (sourceOfFund.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (sourceOfFund.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (sourceOfFund.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(sourceOfFund.getAuthStatus());
		}

		return model;
	}

	private SourceOfFundFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<SourseOfFundModel> data, PageDetails pageDetails) {
		SourceOfFundFetchResponse response = new SourceOfFundFetchResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public SourceOfFundFetchResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** FETCH ALL SOURCE OF FUND DETAILS *************************************");

		try {
			List<SourceOfFund> sourceOfFundList = sourceOfFundRepository.findByStatus("Active");

			if (sourceOfFundList.isEmpty()) {
				return buildErrorResponse(false, "No Source of Fund found.", "01", null, null);
			}

			List<SourseOfFundModel> sourceOfFundData = sourceOfFundList.stream().map(this::convertToSourseOfFundModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", sourceOfFundData, null);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION: ", e);
			return buildErrorResponse(false, "Exception occurred", "03", null, null);
		}
	}

}
