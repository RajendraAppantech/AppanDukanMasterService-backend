package com.appan.planmaster.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.appan.planmaster.entity.RechargePlansMaster;
import com.appan.planmaster.rechargeplans.model.FetchRechargeModel;
import com.appan.planmaster.rechargeplans.model.FetchRechargePlanRequest;
import com.appan.planmaster.rechargeplans.model.FetchRechargePlanResponse;
import com.appan.planmaster.repositories.PlanMasterRepositories.RechargePlansMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchRechargePlanService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private RechargePlansMasterRepository rechargePlansMasterRepository;
	private static final Logger logger = LoggerFactory.getLogger(FetchRechargePlanService.class);

	public FetchRechargePlanResponse fetch(FetchRechargePlanRequest req, Integer pageNo, Integer pageSize) {
		logger.info(
				"\r\n\r\n**************************** FETCH RECHARGE PLAN DETAILS *************************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<RechargePlansMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<RechargePlansMaster> pageResults = rechargePlansMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No recharge plans found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<FetchRechargeModel> rechargeData = pageResults.stream().map(this::convertToRechargeModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", rechargeData, pageDetails);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<RechargePlansMaster> root,
			FetchRechargePlanRequest req) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (req.getPlanId() != null && !req.getPlanId().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("planId")), req.getPlanId().toLowerCase()));
		}
		if (req.getPlanType() != null && !req.getPlanType().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("planType")), req.getPlanType().toLowerCase()));
		}
		if (req.getCircleName() != null && !req.getCircleName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("circleName")), req.getCircleName().toLowerCase()));
		}
		if (req.getOperatorName() != null && !req.getOperatorName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("operatorName")), req.getOperatorName().toLowerCase()));
		}
		if (req.getAmount() != null) {
			predicates.add(cb.equal(root.get("amount"), req.getAmount()));
		}
		if (req.getValidity() != null && !req.getValidity().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("validity")), req.getValidity().toLowerCase()));
		}
		if (req.getStatus() != null && !req.getStatus().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}
		// Date filters
		if (req.getFromDate() != null && req.getToDate() != null) {
			try {
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());

				endDate = new Date(endDate.getTime() + 86400000L);
				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (Exception e) {
				logger.error("Error parsing date filters", e);
			}
		}

		return predicates;
	}

	private FetchRechargeModel convertToRechargeModel(RechargePlansMaster rechargePlan) {
		FetchRechargeModel model = new FetchRechargeModel();
		model.setId(rechargePlan.getId());
		model.setPlanId(rechargePlan.getPlanId());
		model.setPlanType(rechargePlan.getPlanType());
		model.setCircleName(rechargePlan.getCircleName());
		model.setOperatorName(rechargePlan.getOperatorName());
		model.setAmount(rechargePlan.getAmount());
		model.setValidity(rechargePlan.getValidity());
		model.setShortDesc(rechargePlan.getShortDesc());
		model.setLongDesc(rechargePlan.getLongDesc());
		model.setStatus(rechargePlan.getStatus());
		model.setCreatedBy(rechargePlan.getCreatedBy());
		model.setCreatedDt(rechargePlan.getCreatedDt());
		model.setModifyBy(rechargePlan.getModifyBy());
		model.setModifyDt(rechargePlan.getModifyDt());
		model.setAuthBy(rechargePlan.getAuthBy());
		model.setAuthDate(rechargePlan.getAuthDate());
		model.setAuthStatus(rechargePlan.getAuthStatus());
		return model;
	}

	private FetchRechargePlanResponse buildErrorResponse(boolean status, String message, String respCode,
			List<FetchRechargeModel> data, PageDetails pageDetails) {
		FetchRechargePlanResponse response = new FetchRechargePlanResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchRechargePlanResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** GET ALL RECHARGE PLANS *************************************");

		FetchRechargePlanResponse response = new FetchRechargePlanResponse();
		try {
			List<RechargePlansMaster> allPlans = rechargePlansMasterRepository.findByStatus("Active");

			if (allPlans.isEmpty()) {
				return buildErrorResponse(false, "No recharge plans found.", "01", null, null);
			}

			List<FetchRechargeModel> rechargeData = allPlans.stream().map(this::convertToRechargeModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", rechargeData, null);

		} catch (Exception e) {
			logger.error("EXCEPTION: " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
