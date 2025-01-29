package com.appan.businessmaster.services;

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
import com.appan.BusinessEntityType.model.BusinessEntityTypeFetchRequest;
import com.appan.BusinessEntityType.model.BusinessEntityTypeModel;
import com.appan.BusinessEntityType.model.FetchBusinessEntityTypeResponse;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.BusinessEntityType;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessEntityTypeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class BusinessEntityTypeFetchService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private BusinessEntityTypeRepository businessEntityTypeRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessEntityTypeFetchService.class);

	public FetchBusinessEntityTypeResponse fetchBusinessEntityType(BusinessEntityTypeFetchRequest req, Integer pageNo,
			Integer pageSize) {
		logger.info(
				"\r\n\r\n**************************** FETCH BUSINESS ENTITY TYPE DETAILS *************************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<BusinessEntityType> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = null;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<BusinessEntityType> businessEntityTypePage = businessEntityTypeRepository.findAll(specification,
					paging);
			if (businessEntityTypePage.isEmpty()) {
				return buildErrorResponse(false, "Business Entity Type not found", "01", null, null);
			}

			List<BusinessEntityTypeModel> entityTypes = businessEntityTypePage.stream().map(this::convertToModel)
					.collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(businessEntityTypePage.getNumber() + 1);
			pageDetails.setTotalRecords(businessEntityTypePage.getTotalElements());
			pageDetails.setNoOfPages(businessEntityTypePage.getTotalPages());
			pageDetails.setPageSize(businessEntityTypePage.getSize());

			return buildErrorResponse(true, "Success", "00", entityTypes, pageDetails);

		} catch (Exception e) {
			logger.error("Exception: ", e);
			return buildErrorResponse(false, "Exception occurred", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<BusinessEntityType> root,
			BusinessEntityTypeFetchRequest req) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), cb.literal(startDate), cb.literal(endDate)));
		}

		if (!Strings.isNullOrEmpty(req.getEntityTypeName()) && !"all".equalsIgnoreCase(req.getEntityTypeName())) {
			predicates.add(
					cb.like(cb.lower(root.get("entityTypeName")), "%" + req.getEntityTypeName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getCode()) && !"all".equalsIgnoreCase(req.getCode())) {
			predicates.add(cb.like(cb.lower(root.get("code")), "%" + req.getCode().toLowerCase() + "%"));
		}

		if (req.getIsKyc() != null) {
			predicates.add(cb.equal(root.get("isKyc"), req.getIsKyc()));
		}

		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.like(cb.lower(root.get("status")), "%" + req.getStatus().toLowerCase() + "%"));
		}

		return predicates;
	}

	private BusinessEntityTypeModel convertToModel(BusinessEntityType entityType) {
		BusinessEntityTypeModel model = new BusinessEntityTypeModel();
		model.setId(entityType.getId());
		model.setEntityTypeName(entityType.getEntityTypeName());
		model.setCode(entityType.getCode());
		model.setIsKyc(entityType.getIsKyc());
		model.setStatus(entityType.getStatus());
		model.setCreatedDt(entityType.getCreatedDt());
		model.setModifyDt(entityType.getModifyDt());
		return model;
	}

	private FetchBusinessEntityTypeResponse buildErrorResponse(boolean status, String message, String respCode,
			List<BusinessEntityTypeModel> data, PageDetails pageDetails) {
		FetchBusinessEntityTypeResponse response = new FetchBusinessEntityTypeResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchBusinessEntityTypeResponse getAllBusinessEntityTypes() {
		logger.info(
				"\r\n\r\n**************************** GET ALL BUSINESS ENTITY TYPES *************************************");
		FetchBusinessEntityTypeResponse response = new FetchBusinessEntityTypeResponse();

		try {
			List<BusinessEntityType> businessEntityTypeList = businessEntityTypeRepository.findByStatus("Active");

			if (businessEntityTypeList.isEmpty()) {
				return buildErrorResponse(false, "No records found.", "01", null, null);
			}

			List<BusinessEntityTypeModel> data = businessEntityTypeList.stream().map(this::convertToModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", data, null);
		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
