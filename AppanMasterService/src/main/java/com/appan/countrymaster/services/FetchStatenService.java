package com.appan.countrymaster.services;

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
import com.appan.countrymaster.state.models.FetchStateRequest;
import com.appan.countrymaster.state.models.FetchStateResponse;
import com.appan.countrymaster.state.models.StateMasterModel;
import com.appan.entity.StateMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.StateMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchStatenService {

	@Autowired
	private UserMasterRepository masterRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();
	private static final Logger Logger = LoggerFactory.getLogger(FetchStatenService.class);

	@Autowired
	private StateMasteRepository stateMasteRepository;

	public FetchStateResponse fetch(FetchStateRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH STATE DETAILS *************************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<StateMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = null;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getStateName(), req.getCountryName(),
							req.getFingpayStateId(), req.getGiftCardStateId(), req.getStatus(), req.getFromDate(),
							req.getToDate());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<StateMaster> pageResult = stateMasteRepository.findAll(specification, paging);
			if (pageResult.isEmpty()) {
				return buildErrorResponse(false, "State details not found.", "01", null, null);
			}

			List<StateMasterModel> stateModels = pageResult.stream().map(this::convertToStateMasterModel)
					.collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResult.getNumber() + 1);
			pageDetails.setTotalRecords(pageResult.getTotalElements());
			pageDetails.setNoOfPages(pageResult.getTotalPages());
			pageDetails.setPageSize(pageResult.getSize());

			return buildErrorResponse(true, "SUCCESS", "00", stateModels, pageDetails);

		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<StateMaster> root, String stateName,
			String countryName, String fingpayStateId, String giftCardStateId, String authStatus, String fromDate,
			String toDate) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
			Date startDate = dateFormat.parse(fromDate);
			Date endDate = dateFormat.parse(toDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (!Strings.isNullOrEmpty(stateName) && !"all".equalsIgnoreCase(stateName)) {
			predicates.add(cb.like(cb.lower(root.get("stateName")), "%" + stateName.toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(countryName) && !"all".equalsIgnoreCase(countryName)) {
			predicates.add(cb.like(cb.lower(root.get("countryName")), "%" + countryName.toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(fingpayStateId) && !"all".equalsIgnoreCase(fingpayStateId)) {
			predicates.add(cb.equal(root.get("fingpayStateId"), fingpayStateId));
		}
		if (!Strings.isNullOrEmpty(giftCardStateId) && !"all".equalsIgnoreCase(giftCardStateId)) {
			predicates.add(cb.equal(root.get("giftCardStateId"), giftCardStateId));
		}
		if (!Strings.isNullOrEmpty(authStatus) && !"all".equalsIgnoreCase(authStatus)) {
			predicates.add(cb.equal(root.get("authStatus"), authStatus));
		}

		return predicates;
	}

	private StateMasterModel convertToStateMasterModel(StateMaster model) {
		StateMasterModel stateModel = new StateMasterModel();
		stateModel.setId(model.getId());
		stateModel.setStateName(model.getStateName());
		stateModel.setCountryName(model.getCountryName());
		stateModel.setFingpayStateId(model.getFingpayStateId());
		stateModel.setGiftCardStateId(model.getGiftCardStateId());
		stateModel.setStatus(model.getStatus());
		stateModel.setCreatedDt(model.getCreatedDt());
		stateModel.setCreatedBy(model.getCreatedBy());
		stateModel.setModifyBy(model.getModifyBy());
		stateModel.setModifyDt(model.getModifyDt());
		stateModel.setAuthBy(model.getAuthBy());
		stateModel.setAuthDate(model.getAuthDate());
		if (Strings.isNullOrEmpty(model.getAuthStatus())) {
			stateModel.setAuthStatus("UNKNOWN");
		} else if (model.getAuthStatus().equalsIgnoreCase("1")) {
			stateModel.setAuthStatus("ACTIVE");
		} else if (model.getAuthStatus().equalsIgnoreCase("3")) {
			stateModel.setAuthStatus("BLOCK");
		} else if (model.getAuthStatus().equalsIgnoreCase("4")) {
			stateModel.setAuthStatus("IN-ACTIVE");
		} else {
			stateModel.setAuthStatus(model.getAuthStatus());
		}
		return stateModel;
	}

	private FetchStateResponse buildErrorResponse(boolean status, String message, String respCode,
			List<StateMasterModel> data, PageDetails pageDetails) {
		FetchStateResponse response = new FetchStateResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchStateResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL STATE CODE *************************************");
		FetchStateResponse response = new FetchStateResponse();
		try {
			List<StateMaster> stateList = stateMasteRepository.findByStatus("Active");
			if (stateList.isEmpty()) {
				return buildErrorResponse(false, "State details not found.", "01", null, null);
			}

			List<StateMasterModel> stateModels = stateList.stream().map(this::convertToStateMasterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", stateModels, null);

		} catch (Exception e) {
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
