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
import com.appan.countrymaster.pincode.models.FetchPincodeRequest;
import com.appan.countrymaster.pincode.models.FetchPincodeResponse;
import com.appan.countrymaster.pincode.models.PincodeMasterModel;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.PincodeMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.PincodeMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchPincodeService {

	@Autowired
	private UserMasterRepository masterRepository;

	public static final Logger logger = LoggerFactory.getLogger(FetchPincodeService.class);

	@Autowired
	private PincodeMasteRepository pincodeMasteRepository;

	public FetchPincodeResponse fetch(FetchPincodeRequest req, Integer pageNo, Integer pageSize) {
		logger.info("\r\n\r\n**************************** FETCH PINCODE DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<PincodeMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					logger.error("Error parsing date filters", e);
					return null;
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<PincodeMaster> pageResult = pincodeMasteRepository.findAll(specification, paging);
			if (pageResult.isEmpty()) {
				return buildErrorResponse(false, "Pincode details not found.", "01", null, null);
			}

			List<PincodeMasterModel> pincodeModels = pageResult.stream().map(this::convertToPincodeMasterModel)
					.collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResult.getNumber() + 1);
			pageDetails.setTotalRecords(pageResult.getTotalElements());
			pageDetails.setNoOfPages(pageResult.getTotalPages());
			pageDetails.setPageSize(pageResult.getSize());

			return buildErrorResponse(true, "SUCCESS", "00", pincodeModels, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<PincodeMaster> root, FetchPincodeRequest req)
			throws ParseException {
		List<Predicate> predicates = new ArrayList<>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (!Strings.isNullOrEmpty(req.getPincode()) && !"all".equalsIgnoreCase(req.getPincode())) {
			predicates.add(cb.like(cb.lower(root.get("pincode")), "%" + req.getPincode().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getBlockPoName()) && !"all".equalsIgnoreCase(req.getBlockPoName())) {
			predicates.add(cb.like(cb.lower(root.get("blockPoName")), "%" + req.getBlockPoName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.like(cb.lower(root.get("status")), "%" + req.getStatus().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.like(cb.lower(root.get("authStatus")), "%" + req.getStatus().toLowerCase() + "%"));
		}

		return predicates;
	}

	private PincodeMasterModel convertToPincodeMasterModel(PincodeMaster model) {
		PincodeMasterModel pincodeModel = new PincodeMasterModel();
		pincodeModel.setPincodeId(model.getPincodeId());
		pincodeModel.setPincode(model.getPincode());
		pincodeModel.setBlockPoName(model.getBlockPoName());
		pincodeModel.setStatus(model.getStatus());
		pincodeModel.setCreatedDt(model.getCreatedDt());
		pincodeModel.setCreatedBy(model.getCreatedBy());
		pincodeModel.setModifyBy(model.getModifyBy());
		pincodeModel.setModifyDt(model.getModifyDt());
		pincodeModel.setAuthBy(model.getAuthBy());
		pincodeModel.setAuthDate(model.getAuthDate());

		if (Strings.isNullOrEmpty(model.getAuthStatus())) {
			pincodeModel.setAuthStatus("UNKNOWN");
		} else if (model.getAuthStatus().equalsIgnoreCase("1")) {
			pincodeModel.setAuthStatus("ACTIVE");
		} else if (model.getAuthStatus().equalsIgnoreCase("3")) {
			pincodeModel.setAuthStatus("BLOCK");
		} else if (model.getAuthStatus().equalsIgnoreCase("4")) {
			pincodeModel.setAuthStatus("IN-ACTIVE");
		} else {
			pincodeModel.setAuthStatus(model.getAuthStatus());
		}
		return pincodeModel;
	}

	private FetchPincodeResponse buildErrorResponse(boolean status, String message, String respCode,
			List<PincodeMasterModel> data, PageDetails pageDetails) {
		FetchPincodeResponse response = new FetchPincodeResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchPincodeResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** GET ALL PINCODE DETAILS *************************************");
		FetchPincodeResponse response = new FetchPincodeResponse();
		try {
			List<PincodeMaster> pincodeList = pincodeMasteRepository.findByStatus("Active");
			if (pincodeList.isEmpty()) {
				return buildErrorResponse(false, "Pincode details not found.", "01", null, null);
			}

			List<PincodeMasterModel> pincodeModels = pincodeList.stream().map(this::convertToPincodeMasterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", pincodeModels, null);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
