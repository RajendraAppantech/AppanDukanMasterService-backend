package com.appan.switchms.service;

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
import com.appan.entity.SwitchOperatorMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.switchms.models.FetchSwitchOperatorRequest;
import com.appan.switchms.models.FetchSwitchOperatorResponse;
import com.appan.switchms.models.SwitchOperatorMasterModel;
import com.appan.switchms.repositories.SwitchMsRepositories.SwitchOperatorMasterRepository;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchSwitchOperatorService {

	@Autowired
	private UserMasterRepository masterRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(FetchSwitchOperatorService.class);

	@Autowired
	private SwitchOperatorMasterRepository repository;

	public FetchSwitchOperatorResponse fetch(FetchSwitchOperatorRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH SWITCH OPERATOR *************************************");
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<SwitchOperatorMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getUserName(), req.getOperatorName(),
							req.getStatus(), req.getFromDate(), req.getToDate());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (ParseException e) {
					e.printStackTrace();
					Logger.info("EXCEPTION : " + e);
					return null;
				}
			};

			Page<SwitchOperatorMaster> ums = repository.findAll(specification, paging);
			if (ums.isEmpty()) {
				return buildErrorResponse(false, "Switch type not found.", "01", null, null);
			}
			Logger.info("ums : " + ums);
			PageDetails details = new PageDetails();
			details.setPageNo(ums.getNumber() + 1);
			details.setTotalRecords(ums.getTotalElements());
			details.setNoOfPages(ums.getTotalPages());
			details.setPageSize(ums.getSize());

			List<SwitchOperatorMasterModel> fetchUserDatas = ums.stream().map(this::convertToBankModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", fetchUserDatas, details);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private SwitchOperatorMasterModel convertToBankModel(SwitchOperatorMaster model) {
		SwitchOperatorMasterModel bankModel = new SwitchOperatorMasterModel();
		bankModel.setId(model.getId());
		bankModel.setUserName(model.getUserName());
		bankModel.setOperatorName(model.getOperatorName());
		bankModel.setApiCount(model.getApiCount());
		bankModel.setApiName1(model.getApiName1());
		bankModel.setApiName2(model.getApiName2());
		bankModel.setApiName3(model.getApiName3());
		bankModel.setApiName4(model.getApiName4());
		bankModel.setApiName5(model.getApiName5());
		bankModel.setApiName6(model.getApiName6());
		bankModel.setApiName7(model.getApiName7());
		bankModel.setStatus(model.getStatus());
		bankModel.setCreatedBy(model.getCreatedBy());
		bankModel.setCreatedDt(model.getCreatedDt());
		bankModel.setModifyBy(model.getModifyBy());
		bankModel.setModifyDt(model.getModifyDt());
		bankModel.setAuthBy(model.getAuthBy());
		bankModel.setAuthDate(model.getAuthDate());
		if (Strings.isNullOrEmpty(model.getAuthStatus())) {
			bankModel.setAuthStatus("UNKNOWN");
		} else if (model.getAuthStatus().equalsIgnoreCase("1")) {
			bankModel.setAuthStatus("ACTIVE");
		} else if (model.getAuthStatus().equalsIgnoreCase("3")) {
			bankModel.setAuthStatus("BLOCK");
		} else if (model.getAuthStatus().equalsIgnoreCase("4")) {
			bankModel.setAuthStatus("IN-ACTIVE");
		} else {
			bankModel.setAuthStatus(model.getAuthStatus());
		}
		return bankModel;
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<SwitchOperatorMaster> root, String userName,
			String operatorName, String authStatus, String fromDate, String toDate)

			throws ParseException {
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

		if (!Strings.isNullOrEmpty(userName) && (!"all".equalsIgnoreCase(userName))) {
			predicates.add(cb.equal(root.get("userName"), userName));
		}
		if (!Strings.isNullOrEmpty(operatorName) && (!"all".equalsIgnoreCase(operatorName))) {
			predicates.add(cb.equal(root.get("operatorName"), operatorName));
		}
		if (!Strings.isNullOrEmpty(authStatus) && (!"all".equalsIgnoreCase(authStatus))) {
			predicates.add(cb.equal(root.get("authStatus"), authStatus.trim()));
		}
		return predicates;
	}

	private FetchSwitchOperatorResponse buildErrorResponse(boolean status, String message, String respCode,
			List<SwitchOperatorMasterModel> data, PageDetails pageDetails) {
		FetchSwitchOperatorResponse response = new FetchSwitchOperatorResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchSwitchOperatorResponse getAllData() {
		Logger.info(
				"\r\n\r\n**************************** GET ALL SWITCH OPERATOR *************************************");
		try {

			List<SwitchOperatorMaster> retailList = repository.findByStatus("Active");
			if (retailList.isEmpty()) {
				return buildErrorResponse(false, "Region not found.", "01", null, null);
			}

			List<SwitchOperatorMasterModel> fetchUserDatas = retailList.stream().map(this::convertToBankModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", fetchUserDatas, null);

		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}