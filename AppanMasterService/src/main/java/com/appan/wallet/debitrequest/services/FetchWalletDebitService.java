package com.appan.wallet.debitrequest.services;

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
import com.appan.entity.WalletDebitMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.wallet.debitrequest.model.FetchWalletDebitRequest;
import com.appan.wallet.debitrequest.model.FetchWalletDebitResponse;
import com.appan.wallet.debitrequest.model.WalletDebitData;
import com.appan.wallet.managepayment.repo.WalletRepositories.WalletDebitMasterRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

@Service
public class FetchWalletDebitService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private WalletDebitMasterRepository walletDebitMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchWalletDebitService.class);

	public FetchWalletDebitResponse fetch(@Valid FetchWalletDebitRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("**************************** FETCH WALLET DEBIT DETAILS ****************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<WalletDebitMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (Exception e) {
					Logger.error("Exception while building predicates: ", e);
					return null;
				}
			};

			Page<WalletDebitMaster> debitPage = walletDebitMasterRepository.findAll(specification, paging);

			if (debitPage.isEmpty()) {
				return buildErrorResponse(false, "No wallet debit records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(debitPage.getNumber() + 1);
			pageDetails.setTotalRecords(debitPage.getTotalElements());
			pageDetails.setNoOfPages(debitPage.getTotalPages());
			pageDetails.setPageSize(debitPage.getSize());

			List<WalletDebitData> debitData = debitPage.stream().map(this::convertToWalletDebitData)
					.collect(Collectors.toList());

			return buildSuccessResponse(debitData, pageDetails);

		} catch (Exception e) {
			Logger.error("Exception in fetch service: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<WalletDebitMaster> root,
			FetchWalletDebitRequest req) {
		List<Predicate> predicates = new ArrayList<>();

		if (req.getDebitUsername() != null && !req.getDebitUsername().equalsIgnoreCase("all")
				&& !req.getDebitUsername().isEmpty()) {
			predicates.add(
					cb.like(cb.lower(root.get("debitUsername")), "%" + req.getDebitUsername().toLowerCase() + "%"));
		}

		if (req.getCreditUsername() != null && !req.getCreditUsername().equalsIgnoreCase("all")
				&& !req.getCreditUsername().isEmpty()) {
			predicates.add(
					cb.like(cb.lower(root.get("creditUsername")), "%" + req.getCreditUsername().toLowerCase() + "%"));
		}

		if (req.getAmount() != null) {
			predicates.add(cb.equal(root.get("amount"), req.getAmount()));
		}

		if (req.getStatus() != null && !req.getStatus().equalsIgnoreCase("all") && !req.getStatus().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		if (req.getFromDate() != null && req.getToDate() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();
				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				Logger.error("Date parsing exception: ", e);
			}
		}

		return predicates;
	}

	private WalletDebitData convertToWalletDebitData(WalletDebitMaster debit) {
		WalletDebitData data = new WalletDebitData();
		data.setId(debit.getId());
		data.setDebitUsername(debit.getDebitUsername());
		data.setCreditUsername(debit.getCreditUsername());
		data.setAmount(debit.getAmount());
		data.setStatus(debit.getStatus());
		data.setUserComment(debit.getUserComment());
		data.setRemark(debit.getRemark());
		data.setUpdateRemark(debit.getUpdateRemark());
		data.setVerifiedOn(debit.getVerifiedOn());
		data.setVerifiedBy(debit.getVerifiedBy());
		data.setCreatedBy(debit.getCreatedBy());
		data.setCreatedDt(debit.getCreatedDt());
		data.setModifyBy(debit.getModifyBy());
		data.setModifyDt(debit.getModifyDt());
		data.setAuthBy(debit.getAuthBy());
		data.setAuthDate(debit.getAuthDate());
		data.setAuthStatus(debit.getAuthStatus());
		return data;
	}

	private FetchWalletDebitResponse buildErrorResponse(boolean status, String message, String respCode,
			List<WalletDebitData> data, PageDetails pageDetails) {
		FetchWalletDebitResponse response = new FetchWalletDebitResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	private FetchWalletDebitResponse buildSuccessResponse(List<WalletDebitData> data, PageDetails pageDetails) {
		return buildErrorResponse(true, "SUCCESS", "00", data, pageDetails);
	}

	public FetchWalletDebitResponse getAllData() {
		Logger.info("**************************** GET ALL WALLET DEBIT DATA ****************************");
		FetchWalletDebitResponse response = new FetchWalletDebitResponse();
		try {
			List<WalletDebitMaster> debits = walletDebitMasterRepository.findByAuthStatus("1");
			if (debits.isEmpty()) {
				return buildErrorResponse(false, "No wallet debit records found.", "01", null, null);
			}

			List<WalletDebitData> debitData = debits.stream().map(this::convertToWalletDebitData)
					.collect(Collectors.toList());

			return buildSuccessResponse(debitData, null);
		} catch (Exception e) {
			Logger.error("Exception in getAllData: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
