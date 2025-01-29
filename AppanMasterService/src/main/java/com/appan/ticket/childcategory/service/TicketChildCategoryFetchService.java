package com.appan.ticket.childcategory.service;

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
import com.appan.entity.TicketChildCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TicketChildCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.ticket.childcategory.models.TicketChildCategoryFetchRequest;
import com.appan.ticket.childcategory.models.TicketChildCategoryFetchResponse;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class TicketChildCategoryFetchService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(TicketChildCategoryFetchService.class);

	@Autowired
	private TicketChildCategoryRepository repository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public TicketChildCategoryFetchResponse fetch(TicketChildCategoryFetchRequest req, Integer pageNo,
			Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH DETAILS *************************************");
		try {

			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<TicketChildCategory> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getChildCategoryName(),
							req.getSubCategory(), req.getStatus());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (ParseException e) {
					e.printStackTrace();
					Logger.info("EXCEPTION : " + e);
					return null;
				}
			};

			Page<TicketChildCategory> ums = repository.findAll(specification, paging);
			if (ums.isEmpty()) {
				return buildErrorResponse(false, "Channel not found.", "01", null, null);
			}
			Logger.info("ums : " + ums);
			PageDetails details = new PageDetails();
			details.setPageNo(ums.getNumber() + 1);
			details.setTotalRecords(ums.getTotalElements());
			details.setNoOfPages(ums.getTotalPages());
			details.setPageSize(ums.getSize());

			List<TicketChildCategory> fetchUserDatas = ums.stream().map(this::convertToBankModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", fetchUserDatas, details);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private TicketChildCategory convertToBankModel(TicketChildCategory model) {
		TicketChildCategory bankModel = model;

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

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<TicketChildCategory> root,
			String childCategoryName, String subCategory, String authStatus)

			throws ParseException {
		List<Predicate> predicates = new ArrayList<>();

		if (!Strings.isNullOrEmpty(childCategoryName) && (!"all".equalsIgnoreCase(childCategoryName))) {
			predicates.add(cb.equal(root.get("childCategoryName"), childCategoryName));
		}

		if (!Strings.isNullOrEmpty(subCategory) && (!"all".equalsIgnoreCase(subCategory))) {
			predicates.add(cb.equal(root.get("subCategory"), subCategory));
		}

		if (!Strings.isNullOrEmpty(authStatus) && (!"all".equalsIgnoreCase(authStatus))) {
			predicates.add(cb.equal(root.get("authStatus"), authStatus.trim()));
		}
		return predicates;
	}

	private TicketChildCategoryFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<TicketChildCategory> data, PageDetails pageDetails) {
		TicketChildCategoryFetchResponse response = new TicketChildCategoryFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public TicketChildCategoryFetchResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL CUSTOMER CODE *************************************");
		TicketChildCategoryFetchResponse response = new TicketChildCategoryFetchResponse();
		try {

			List<TicketChildCategory> retailList = repository.findAll();
			if (retailList.isEmpty()) {
				return buildErrorResponse(false, "Channel not found.", "01", null, null);
			}

			List<TicketChildCategory> fetchUserDatas = retailList.stream().map(this::convertToBankModel)
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