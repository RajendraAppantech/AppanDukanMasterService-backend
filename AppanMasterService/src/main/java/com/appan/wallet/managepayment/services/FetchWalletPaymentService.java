package com.appan.wallet.managepayment.services;

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
import com.appan.entity.WalletPaymentMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.wallet.managepayment.model.FetchWalletPaymentRequest;
import com.appan.wallet.managepayment.model.FetchWalletPaymentResponse;
import com.appan.wallet.managepayment.model.WalletPaymentData;
import com.appan.wallet.managepayment.repo.WalletRepositories.WalletPaymentMasterRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

@Service
public class FetchWalletPaymentService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private WalletPaymentMasterRepository walletPaymentMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchWalletPaymentService.class);

	public FetchWalletPaymentResponse fetch(@Valid FetchWalletPaymentRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("**************************** FETCH WALLET PAYMENT DETAILS ****************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<WalletPaymentMaster> specification = (root, query, criteriaBuilder) -> {
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

			Page<WalletPaymentMaster> paymentPage = walletPaymentMasterRepository.findAll(specification, paging);

			if (paymentPage.isEmpty()) {
				return buildErrorResponse(false, "No wallet payment records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(paymentPage.getNumber() + 1);
			pageDetails.setTotalRecords(paymentPage.getTotalElements());
			pageDetails.setNoOfPages(paymentPage.getTotalPages());
			pageDetails.setPageSize(paymentPage.getSize());

			List<WalletPaymentData> paymentData = paymentPage.stream().map(this::convertToWalletPaymentData)
					.collect(Collectors.toList());

			return buildSuccessResponse(paymentData, pageDetails);

		} catch (Exception e) {
			Logger.error("Exception in fetch service: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<WalletPaymentMaster> root, FetchWalletPaymentRequest req) throws ParseException {
	    List<Predicate> predicates = new ArrayList<>();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	    if (req.getFromDate() != null && req.getToDate() != null) {
	        Date startDate = dateFormat.parse(req.getFromDate());
	        Date endDate = dateFormat.parse(req.getToDate());
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(endDate);
	        cal.add(Calendar.DAY_OF_YEAR, 1);
	        endDate = cal.getTime();
	        predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
	    }

	    if (req.getAmount() != null) {
	        predicates.add(cb.equal(root.get("amount"), req.getAmount()));
	    }

	    if (req.getDepositBank() != null && !req.getDepositBank().equalsIgnoreCase("all") && !req.getDepositBank().isEmpty()) {
	        predicates.add(cb.like(cb.lower(root.get("depositBank")), "%" + req.getDepositBank().toLowerCase() + "%"));
	    }

	    if (req.getPaymentMode() != null && !req.getPaymentMode().equalsIgnoreCase("all") && !req.getPaymentMode().isEmpty()) {
	        predicates.add(cb.equal(cb.lower(root.get("paymentMode")), req.getPaymentMode().toLowerCase()));
	    }

	    if (req.getStatus() != null && !req.getStatus().equalsIgnoreCase("all") && !req.getStatus().isEmpty()) {
	        predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
	    }

	    if (req.getUserName() != null && !req.getUserName().equalsIgnoreCase("all") && !req.getUserName().isEmpty()) {
	        predicates.add(cb.like(cb.lower(root.get("userName")), "%" + req.getUserName().toLowerCase() + "%"));
	    }

	    return predicates;
	}

	private WalletPaymentData convertToWalletPaymentData(WalletPaymentMaster payment) {
		WalletPaymentData data = new WalletPaymentData();
		data.setId(payment.getId());
		data.setUserName(payment.getUserName());
		data.setDepositBank(payment.getDepositBank());
		data.setAmount(payment.getAmount());
		data.setPaymentMode(payment.getPaymentMode());
		data.setPaymentDate(payment.getPaymentDate());
		data.setRequestedDate(payment.getRequestedDate());
		data.setStatus(payment.getStatus());
		data.setRefNo(payment.getRefNo());
		data.setPaymentProof(payment.getPaymentProof());
		data.setCashType(payment.getCashType());
		data.setChequeNumber(payment.getChequeNumber());
		data.setUserRemark(payment.getUserRemark());
		data.setUpdateRemark(payment.getUpdateRemark());
		data.setResponse(payment.getResponse());
		data.setResponseTime(payment.getResponseTime());
		data.setCreatedBy(payment.getCreatedBy());
		data.setCreatedDt(payment.getCreatedDt());
		data.setModifyBy(payment.getModifyBy());
		data.setModifyDt(payment.getModifyDt());
		data.setAuthBy(payment.getAuthBy());
		data.setAuthDate(payment.getAuthDate());
		data.setAuthStatus(payment.getAuthStatus());
		return data;
	}

	private FetchWalletPaymentResponse buildErrorResponse(boolean status, String message, String respCode,
			List<WalletPaymentData> data, PageDetails pageDetails) {
		FetchWalletPaymentResponse response = new FetchWalletPaymentResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	private FetchWalletPaymentResponse buildSuccessResponse(List<WalletPaymentData> data, PageDetails pageDetails) {
		return buildErrorResponse(true, "SUCCESS", "00", data, pageDetails);
	}

	public FetchWalletPaymentResponse getAllData() {
		Logger.info("**************************** GET ALL WALLET PAYMENT DATA ****************************");
		FetchWalletPaymentResponse response = new FetchWalletPaymentResponse();
		try {
			List<WalletPaymentMaster> payments = walletPaymentMasterRepository.findByAuthStatus("1");
			if (payments.isEmpty()) {
				return buildErrorResponse(false, "No wallet payment records found.", "01", null, null);
			}

			List<WalletPaymentData> paymentData = payments.stream().map(this::convertToWalletPaymentData)
					.collect(Collectors.toList());

			return buildSuccessResponse(paymentData, null);
		} catch (Exception e) {
			Logger.error("Exception in getAllData: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
