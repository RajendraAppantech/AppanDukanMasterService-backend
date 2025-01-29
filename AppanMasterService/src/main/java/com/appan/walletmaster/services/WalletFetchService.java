package com.appan.walletmaster.services;

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
import com.appan.entity.UserMaster;
import com.appan.entity.WalletMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.WalletMasterRepository;
import com.appan.walletmaster.model.WalletFetchRequest;
import com.appan.walletmaster.model.WalletFetchResponse;
import com.appan.walletmaster.model.WalletMasterModel;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class WalletFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private WalletMasterRepository walletMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(WalletFetchService.class);

	public WalletFetchResponse fetchWallet(WalletFetchRequest req, Integer pageNo, Integer pageSize) {
		logger.info("**************************** FETCH WALLET DETAILS *************************************");

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<WalletMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getWalletName(), req.getCode(),
						req.getDescription(), req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<WalletMaster> pageResults = walletMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Wallets not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<WalletMasterModel> walletData = pageResults.stream().map(this::convertToWalletModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", walletData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<WalletMaster> root, String walletName, String code,
			String description, String status, String fromDate, String toDate) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (fromDate != null && toDate != null) {
			try {
				Date startDate = dateFormat.parse(fromDate);
				Date endDate = dateFormat.parse(toDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				logger.error("Error parsing date filters", e);
			}
		}

		if (walletName != null && !walletName.isEmpty() && !"all".equalsIgnoreCase(walletName)) {
			predicates.add(cb.equal(cb.lower(root.get("walletName")), walletName.toLowerCase()));
		}
		if (code != null && !code.isEmpty() && !"all".equalsIgnoreCase(code)) {
			predicates.add(cb.equal(root.get("code"), code));
		}
		if (description != null && !description.isEmpty() && !"all".equalsIgnoreCase(description)) {
			predicates.add(cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private WalletMasterModel convertToWalletModel(WalletMaster walletMaster) {
		WalletMasterModel model = new WalletMasterModel();
		model.setId(walletMaster.getId());
		model.setWalletName(walletMaster.getWalletName());
		model.setCode(walletMaster.getCode());
		model.setDescription(walletMaster.getDescription());
		model.setStatus(walletMaster.getStatus());
		model.setCreatedBy(walletMaster.getCreatedBy());
		model.setCreatedDt(walletMaster.getCreatedDt());
		model.setModifyBy(walletMaster.getModifyBy());
		model.setModifyDt(walletMaster.getModifyDt());
		model.setAuthBy(walletMaster.getAuthBy());
		model.setAuthDate(walletMaster.getAuthDate());

		if (Strings.isNullOrEmpty(walletMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (walletMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (walletMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (walletMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(walletMaster.getAuthStatus());
		}

		return model;
	}

	private WalletFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<WalletMasterModel> data, PageDetails pageDetails) {
		WalletFetchResponse response = new WalletFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public WalletFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL WALLET  *************************************");

		WalletFetchResponse response = new WalletFetchResponse();
		try {
			List<WalletMaster> allwallet = walletMasterRepository.findByStatus("Active");
			if (allwallet.isEmpty()) {
				return buildErrorResponse(false, "No wallet found.", "01", null, null);
			}

			List<WalletMasterModel> walletData = allwallet.stream().map(this::convertToWalletModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", walletData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
