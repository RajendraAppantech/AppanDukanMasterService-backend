package com.appan.wallet.bulk.services;

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
import com.appan.entity.WalletBulkDebitCredit;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.wallet.bulk.model.FetchWalletBulkDebitCreditRequest;
import com.appan.wallet.bulk.model.FetchWalletBulkDebitCreditResponse;
import com.appan.wallet.managepayment.repo.WalletRepositories.WalletBulkDebitCreditRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchWalletBulkDebitCreditService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private WalletBulkDebitCreditRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(FetchWalletBulkDebitCreditService.class);

	public FetchWalletBulkDebitCreditResponse fetch(FetchWalletBulkDebitCreditRequest req, Integer pageNo,
			Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<WalletBulkDebitCredit> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getStatus(), req.getFromDate(),
						req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<WalletBulkDebitCredit> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Steps not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<WalletBulkDebitCredit> StepsData = pageResults.stream().map(this::convertToStepsModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", StepsData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<WalletBulkDebitCredit> root, String status,
			String fromDate, String toDate) {
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

		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private WalletBulkDebitCredit convertToStepsModel(WalletBulkDebitCredit WalletBulkDebitCredit) {
		WalletBulkDebitCredit model = WalletBulkDebitCredit;
		return model;
	}

	private FetchWalletBulkDebitCreditResponse buildErrorResponse(boolean status, String message, String respCode,
			List<WalletBulkDebitCredit> data, PageDetails pageDetails) {
		FetchWalletBulkDebitCreditResponse response = new FetchWalletBulkDebitCreditResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}
}