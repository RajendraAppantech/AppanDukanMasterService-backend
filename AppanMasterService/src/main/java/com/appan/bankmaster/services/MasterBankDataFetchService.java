package com.appan.bankmaster.services;

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
import com.appan.bankmaster.entity.MasterBankData;
import com.appan.bankmaster.masterbankdata.model.FetchMasterBankDataModel;
import com.appan.bankmaster.masterbankdata.model.FetchMasterBankDataResponse;
import com.appan.bankmaster.masterbankdata.model.MasterBankDataFetchRequest;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.MasterBankDataRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class MasterBankDataFetchService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger logger = LoggerFactory.getLogger(MasterBankDataFetchService.class);

	@Autowired
	private MasterBankDataRepository masterBankDataRepository;

	public FetchMasterBankDataResponse fetchMasterBankData(MasterBankDataFetchRequest req, Integer pageNo,
			Integer pageSize) {
		logger.info(
				"\r\n\r\n**************************** FETCH MASTER BANK DATA *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<MasterBankData> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = null;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<MasterBankData> bankDataPage = masterBankDataRepository.findAll(specification, paging);
			if (bankDataPage.isEmpty()) {
				return buildErrorResponse(false, "No records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(bankDataPage.getNumber() + 1);
			pageDetails.setTotalRecords(bankDataPage.getTotalElements());
			pageDetails.setNoOfPages(bankDataPage.getTotalPages());
			pageDetails.setPageSize(bankDataPage.getSize());

			List<FetchMasterBankDataModel> bankDataList = bankDataPage.stream()
					.map(this::convertToFetchMasterBankDataModel).collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", bankDataList, pageDetails);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception: ", e);
			return buildErrorResponse(false, "Exception occurred.", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<MasterBankData> root,
			MasterBankDataFetchRequest req) throws ParseException {
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

		if (!Strings.isNullOrEmpty(req.getBankName())) {
			predicates.add(cb.equal(root.get("bankName"), req.getBankName()));
		}
		if (!Strings.isNullOrEmpty(req.getIfscCode())) {
			predicates.add(cb.equal(root.get("ifscCode"), req.getIfscCode()));
		}
		if (!Strings.isNullOrEmpty(req.getBranch())) {
			predicates.add(cb.equal(root.get("branch"), req.getBranch()));
		}

		return predicates;
	}

	private FetchMasterBankDataModel convertToFetchMasterBankDataModel(MasterBankData bankData) {
		FetchMasterBankDataModel model = new FetchMasterBankDataModel();
		model.setId(bankData.getId());
		model.setBankName(bankData.getBankName());
		model.setIfscCode(bankData.getIfscCode());
		model.setBranch(bankData.getBranch());
		model.setAddress(bankData.getAddress());
		model.setContact(bankData.getContact());
		model.setCity(bankData.getCity());
		model.setState(bankData.getState());
		model.setBlock(bankData.getBlock());
		model.setCreatedBy(bankData.getCreatedBy());
		model.setCreatedDt(bankData.getCreatedDt());
		model.setModifyBy(bankData.getModifyBy());
		model.setModifyDt(bankData.getModifyDt());
		model.setAuthBy(bankData.getAuthBy());
		model.setAuthDate(bankData.getAuthDate());
		if (Strings.isNullOrEmpty(bankData.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (bankData.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (bankData.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (bankData.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(bankData.getAuthStatus());
		}

		return model;
	}

	private FetchMasterBankDataResponse buildErrorResponse(boolean status, String message, String respCode,
			List<FetchMasterBankDataModel> data, PageDetails pageDetails) {
		FetchMasterBankDataResponse response = new FetchMasterBankDataResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchMasterBankDataResponse getAllMasterBankData() {
		logger.info(
				"\r\n\r\n**************************** GET ALL MASTER BANK DATA *************************************");
		FetchMasterBankDataResponse response = new FetchMasterBankDataResponse();

		try {
			List<MasterBankData> masterBankDataList = masterBankDataRepository.findAll();

			if (masterBankDataList.isEmpty()) {
				return buildErrorResponse(false, "No authorized records found.", "01", null, null);
			}

			List<FetchMasterBankDataModel> bankDataList = masterBankDataList.stream()
					.map(this::convertToFetchMasterBankDataModel).collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", bankDataList, null);

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred.");
			response.setRespCode("EX");
			return response;
		}
	}

}
