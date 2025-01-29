package com.appan.tenant.services;

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
import com.appan.entity.TenantManagementMaster;
import com.appan.repositories.Repositories.TenantManagementMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.tenant.model.FetchTenantRequest;
import com.appan.tenant.model.FetchTenantResponse;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

@Service
public class FetchTenantService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TenantManagementMasterRepository tenantRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchTenantService.class);

	public FetchTenantResponse fetch(@Valid FetchTenantRequest req, Integer pageNo, Integer pageSize) {
		try {
			if (userMasterRepository.findByUserId(req.getUsername().toUpperCase()) == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<TenantManagementMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<TenantManagementMaster> tenantPage = tenantRepository.findAll(specification, paging);

			if (tenantPage.isEmpty()) {
				return buildErrorResponse(false, "No tenant records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(tenantPage.getNumber() + 1);
			pageDetails.setTotalRecords(tenantPage.getTotalElements());
			pageDetails.setNoOfPages(tenantPage.getTotalPages());
			pageDetails.setPageSize(tenantPage.getSize());

			List<TenantManagementMaster> tenantData = tenantPage.stream().map(this::convertToModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", tenantData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<TenantManagementMaster> root,
			FetchTenantRequest req) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (req.getTenantName() != null && !req.getTenantName().isEmpty()) {
			predicates.add(cb.like(cb.lower(root.get("tenantName")), "%" + req.getTenantName().toLowerCase() + "%"));
		}
		if (req.getDomainName() != null && !req.getDomainName().isEmpty()) {
			predicates.add(cb.like(cb.lower(root.get("domainName")), "%" + req.getDomainName().toLowerCase() + "%"));
		}
		if (req.getVersion() != null && !req.getVersion().isEmpty()) {
			predicates.add(cb.equal(root.get("version"), req.getVersion()));
		}
		if (req.getStatus() != null && !req.getStatus().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		if (req.getFromDate() != null && req.getToDate() != null) {
			try {
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

	private TenantManagementMaster convertToModel(TenantManagementMaster TenantManagementMaster) {
		TenantManagementMaster model = TenantManagementMaster;
		if (Strings.isNullOrEmpty(TenantManagementMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (TenantManagementMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (TenantManagementMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (TenantManagementMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(TenantManagementMaster.getAuthStatus());
		}
		return model;
	}

	private FetchTenantResponse buildErrorResponse(boolean status, String message, String respCode,
			List<TenantManagementMaster> data, PageDetails pageDetails) {
		FetchTenantResponse response = new FetchTenantResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchTenantResponse getAllData() {
		try {
			List<TenantManagementMaster> tenantList = tenantRepository.findAll();
			if (tenantList.isEmpty()) {
				return buildErrorResponse(false, "No tenant records found.", "01", null, null);
			}

			List<TenantManagementMaster> tenantData = tenantList.stream().map(this::convertToModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", tenantData, null);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}
