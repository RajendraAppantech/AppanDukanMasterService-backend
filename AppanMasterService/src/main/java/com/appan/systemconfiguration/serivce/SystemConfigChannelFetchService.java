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
import com.appan.entity.SystemConfigChannel;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigChannelFetchRequest;
import com.appan.systemconfiguration.models.SystemConfigChannelFetchResponse;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigChannelRepository;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class SystemConfigChannelFetchService {

	@Autowired
	private UserMasterRepository masterRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(SystemConfigChannelFetchService.class);

	@Autowired
	private SystemConfigChannelRepository repository;

	public SystemConfigChannelFetchResponse fetch(SystemConfigChannelFetchRequest req, Integer pageNo,
			Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH DETAILS *************************************");
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<SystemConfigChannel> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getChannelName(), req.getStatus());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (ParseException e) {
					e.printStackTrace();
					Logger.info("EXCEPTION : " + e);
					return null;
				}
			};

			Page<SystemConfigChannel> ums = repository.findAll(specification, paging);
			if (ums.isEmpty()) {
				return buildErrorResponse(false, "Channel not found.", "01", null, null);
			}
			Logger.info("ums : " + ums);
			PageDetails details = new PageDetails();
			details.setPageNo(ums.getNumber() + 1);
			details.setTotalRecords(ums.getTotalElements());
			details.setNoOfPages(ums.getTotalPages());
			details.setPageSize(ums.getSize());

			List<SystemConfigChannel> fetchUserDatas = ums.stream().map(this::convertToBankModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", fetchUserDatas, details);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private SystemConfigChannel convertToBankModel(SystemConfigChannel model) {
		SystemConfigChannel bankModel = model;

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

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<SystemConfigChannel> root, String channelName,
			String authStatus)

			throws ParseException {
		List<Predicate> predicates = new ArrayList<>();

		if (!Strings.isNullOrEmpty(channelName) && (!"all".equalsIgnoreCase(channelName))) {
			predicates.add(cb.equal(cb.lower(root.get("channelName")), channelName.toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(authStatus) && (!"all".equalsIgnoreCase(authStatus))) {
			predicates.add(cb.equal(root.get("authStatus"), authStatus.trim()));
		}
		return predicates;
	}

	private SystemConfigChannelFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<SystemConfigChannel> data, PageDetails pageDetails) {
		SystemConfigChannelFetchResponse response = new SystemConfigChannelFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public SystemConfigChannelFetchResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL CUSTOMER CODE *************************************");
		SystemConfigChannelFetchResponse response = new SystemConfigChannelFetchResponse();
		try {

			List<SystemConfigChannel> retailList = repository.findByStatus("Active");
			if (retailList.isEmpty()) {
				return buildErrorResponse(false, "Channel not found.", "01", null, null);
			}

			List<SystemConfigChannel> fetchUserDatas = retailList.stream().map(this::convertToBankModel)
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