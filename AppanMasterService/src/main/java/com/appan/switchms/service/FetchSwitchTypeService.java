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
import com.appan.entity.SwitchTypeMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.switchms.models.FetchSwitchTypeRequest;
import com.appan.switchms.models.FetchSwitchTypeResponse;
import com.appan.switchms.models.SwitchTypeMasterModel;
import com.appan.switchms.repositories.SwitchMsRepositories.SwitchTypeMasterRepository;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchSwitchTypeService {

	@Autowired
	private UserMasterRepository masterRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(FetchSwitchTypeService.class);

	@Autowired
	private SwitchTypeMasterRepository repository;

	public FetchSwitchTypeResponse fetch(FetchSwitchTypeRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH SWITCH TYPE *************************************");
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<SwitchTypeMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getSwitchType(), req.getPriority(),
							req.getCode(), req.getStatus(), req.getFromDate(), req.getToDate());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (ParseException e) {
					e.printStackTrace();
					Logger.info("EXCEPTION : " + e);
					return null;
				}
			};

			Page<SwitchTypeMaster> ums = repository.findAll(specification, paging);
			if (ums.isEmpty()) {
				return buildErrorResponse(false, "Switch type not found.", "01", null, null);
			}
			Logger.info("ums : " + ums);
			PageDetails details = new PageDetails();
			details.setPageNo(ums.getNumber() + 1);
			details.setTotalRecords(ums.getTotalElements());
			details.setNoOfPages(ums.getTotalPages());
			details.setPageSize(ums.getSize());

			List<SwitchTypeMasterModel> fetchUserDatas = ums.stream().map(this::convertToBankModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", fetchUserDatas, details);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private SwitchTypeMasterModel convertToBankModel(SwitchTypeMaster model) {
		SwitchTypeMasterModel bankModel = new SwitchTypeMasterModel();
		bankModel.setId(model.getId());
		bankModel.setSwitchType(model.getSwitchType());
		bankModel.setPriority(model.getPriority());
		bankModel.setCode(model.getCode());
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

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<SwitchTypeMaster> root, String switchType,
			String priority, String code, String authStatus, String fromDate, String toDate)

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

		if (!Strings.isNullOrEmpty(switchType) && (!"all".equalsIgnoreCase(switchType))) {
			predicates.add(cb.equal(root.get("switchType"), switchType));
		}
		if (!Strings.isNullOrEmpty(priority) && (!"all".equalsIgnoreCase(priority))) {
			predicates.add(cb.equal(root.get("priority"), priority));
		}
		if (!Strings.isNullOrEmpty(code) && (!"all".equalsIgnoreCase(code))) {
			predicates.add(cb.equal(root.get("code"), code));
		}
		if (!Strings.isNullOrEmpty(authStatus) && (!"all".equalsIgnoreCase(authStatus))) {
			predicates.add(cb.equal(root.get("authStatus"), authStatus.trim()));
		}
		return predicates;
	}

	private FetchSwitchTypeResponse buildErrorResponse(boolean status, String message, String respCode,
			List<SwitchTypeMasterModel> data, PageDetails pageDetails) {
		FetchSwitchTypeResponse response = new FetchSwitchTypeResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchSwitchTypeResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL SWITCH TYPE *************************************");
		try {

			List<SwitchTypeMaster> retailList = repository.findByStatus("Active");
			if (retailList.isEmpty()) {
				return buildErrorResponse(false, "Switch Type not found.", "01", null, null);
			}

			List<SwitchTypeMasterModel> fetchUserDatas = retailList.stream().map(this::convertToBankModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", fetchUserDatas, null);

		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}