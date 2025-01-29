package com.appan.systemconfiguration.serivce;

import java.text.ParseException;
import java.util.ArrayList;
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
import com.appan.entity.SystemConfigErrorDefination;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigErrorDefinationFetchRequest;
import com.appan.systemconfiguration.models.SystemConfigErrorDefinationFetchResponse;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigErrorDefinationRepository;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class SystemConfigErrorDefinationFetchService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(SystemConfigErrorDefinationFetchService.class);

	@Autowired
	private SystemConfigErrorDefinationRepository repository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public SystemConfigErrorDefinationFetchResponse fetch(SystemConfigErrorDefinationFetchRequest req, Integer pageNo,
			Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH DETAILS *************************************");
		try {

			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<SystemConfigErrorDefination> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getErrorCode(), req.getErrorMessage(),
							req.getErrorType(), req.getErrorDesc(), req.getStatus());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (ParseException e) {
					e.printStackTrace();
					Logger.info("EXCEPTION : " + e);
					return null;
				}
			};

			Page<SystemConfigErrorDefination> ums = repository.findAll(specification, paging);
			if (ums.isEmpty()) {
				return buildErrorResponse(false, "ErrorDefination not found.", "01", null, null);
			}
			Logger.info("ums : " + ums);
			PageDetails details = new PageDetails();
			details.setPageNo(ums.getNumber() + 1);
			details.setTotalRecords(ums.getTotalElements());
			details.setNoOfPages(ums.getTotalPages());
			details.setPageSize(ums.getSize());

			List<SystemConfigErrorDefination> fetchUserDatas = ums.stream().map(this::convertToBankModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", fetchUserDatas, details);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private SystemConfigErrorDefination convertToBankModel(SystemConfigErrorDefination model) {
		SystemConfigErrorDefination bankModel = model;

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

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<SystemConfigErrorDefination> root,
			String errorCode, String errorMessage, String errorType, String errorDesc, String authStatus)
			throws ParseException {

		List<Predicate> predicates = new ArrayList<>();

		if (!Strings.isNullOrEmpty(errorCode) && !"all".equalsIgnoreCase(errorCode)) {
			predicates.add(cb.equal(cb.lower(root.get("errorCode")), errorCode.trim().toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(errorMessage) && !"all".equalsIgnoreCase(errorMessage)) {
			predicates.add(cb.equal(cb.lower(root.get("errorMessage")), errorMessage.trim().toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(errorDesc) && !"all".equalsIgnoreCase(errorDesc)) {
			predicates.add(cb.equal(cb.lower(root.get("errorDesc")), errorDesc.trim().toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(authStatus) && !"all".equalsIgnoreCase(authStatus)) {
			predicates.add(cb.equal(cb.lower(root.get("authStatus")), authStatus.trim().toLowerCase()));
		}

		return predicates;
	}

	private SystemConfigErrorDefinationFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<SystemConfigErrorDefination> data, PageDetails pageDetails) {
		SystemConfigErrorDefinationFetchResponse response = new SystemConfigErrorDefinationFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public SystemConfigErrorDefinationFetchResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL CUSTOMER CODE *************************************");
		SystemConfigErrorDefinationFetchResponse response = new SystemConfigErrorDefinationFetchResponse();
		try {

			List<SystemConfigErrorDefination> retailList = repository.findAll();
			if (retailList.isEmpty()) {
				return buildErrorResponse(false, "ErrorDefination not found.", "01", null, null);
			}

			List<SystemConfigErrorDefination> fetchUserDatas = retailList.stream().map(this::convertToBankModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", fetchUserDatas, null);

		} catch (Exception e) {
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}