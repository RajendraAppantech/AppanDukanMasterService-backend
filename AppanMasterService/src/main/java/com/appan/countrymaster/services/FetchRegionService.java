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
import com.appan.countrymaster.region.models.FetchRegionRequest;
import com.appan.countrymaster.region.models.FetchRegionResponse;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.countrymaster.region.models.RegionMasterModel;
import com.appan.entity.RegionMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.RegionMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchRegionService {

	@Autowired
	private UserMasterRepository masterRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(FetchRegionService.class);

	@Autowired
	private RegionMasteRepository repository;

	public FetchRegionResponse fetch(FetchRegionRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH REGION DETAILS *************************************");
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<RegionMaster> specification = (root, query, criteriaBuilder) -> {
				try {
					List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getRegionName(),
							req.getRegionType(), req.getStatus(), req.getFromDate(), req.getToDate());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (ParseException e) {
					e.printStackTrace();
					Logger.info("EXCEPTION : " + e);
					return null;
				}
			};

			Page<RegionMaster> ums = repository.findAll(specification, paging);
			if (ums.isEmpty()) {
				return buildErrorResponse(false, "Region not found.", "01", null, null);
			}

			Logger.info("ums : " + ums);
			PageDetails details = new PageDetails();
			details.setPageNo(ums.getNumber() + 1);
			details.setTotalRecords(ums.getTotalElements());
			details.setNoOfPages(ums.getTotalPages());
			details.setPageSize(ums.getSize());

			List<RegionMasterModel> fetchUserDatas = ums.stream().map(this::convertToBankModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", fetchUserDatas, details);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private RegionMasterModel convertToBankModel(RegionMaster model) {
		RegionMasterModel bankModel = new RegionMasterModel();
		bankModel.setRegionId(model.getRegionId());
		bankModel.setRegionName(model.getRegionName());
		bankModel.setRegionType(model.getRegionType());
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

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<RegionMaster> root, String regionName,
			String regionType, String authStatus, String fromDate, String toDate) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(regionName) && !"all".equalsIgnoreCase(regionName)) {
			predicates.add(cb.equal(cb.upper(root.get("regionName")), regionName.trim().toUpperCase()));
		}

		if (!Strings.isNullOrEmpty(regionType) && !"all".equalsIgnoreCase(regionType)) {
			predicates.add(cb.equal(cb.upper(root.get("regionType")), regionType.trim().toUpperCase()));
		}

		if (!Strings.isNullOrEmpty(authStatus) && !"all".equalsIgnoreCase(authStatus)) {
			predicates.add(cb.equal(cb.upper(root.get("authStatus")), authStatus.trim().toUpperCase()));
		}

		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
			Date startDate = dateFormat.parse(fromDate);
			Date endDate = dateFormat.parse(toDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		return predicates;
	}

	private FetchRegionResponse buildErrorResponse(boolean status, String message, String respCode,
			List<RegionMasterModel> data, PageDetails pageDetails) {
		FetchRegionResponse response = new FetchRegionResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchRegionResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL REGION CODE *************************************");
		FetchRegionResponse response = new FetchRegionResponse();
		try {
			List<RegionMaster> retailList = repository.findByStatus("Active");
			if (retailList.isEmpty()) {
				return buildErrorResponse(false, "Region not found.", "01", null, null);
			}

			List<RegionMasterModel> fetchUserDatas = retailList.stream().map(this::convertToBankModel)
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
