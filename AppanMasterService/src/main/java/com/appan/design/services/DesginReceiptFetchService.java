package com.appan.design.services;

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
import com.appan.design.models.DesginReceiptFetchRequest;
import com.appan.design.models.DesginReceiptFetchResponse;
import com.appan.entity.DesignReceiptsMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.DesignReceiptsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DesginReceiptFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DesignReceiptsMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(DesginReceiptFetchService.class);

	public DesginReceiptFetchResponse fetch(DesginReceiptFetchRequest req, Integer pageNo, Integer pageSize) {

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<DesignReceiptsMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<DesignReceiptsMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No Design Receipts found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<DesignReceiptsMaster> data = pageResults.stream().map(this::convertToReceiptModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", data, pageDetails);
		} catch (Exception e) {
			logger.error("Exception occurred while fetching design receipts: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<DesignReceiptsMaster> root,
			DesginReceiptFetchRequest req) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		try {
			if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();
				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			}
		} catch (ParseException e) {
			logger.error("Error parsing date filters: ", e);
		}

		if (!Strings.isNullOrEmpty(req.getDesign()) && !"all".equalsIgnoreCase(req.getDesign())) {
			predicates.add(cb.equal(cb.lower(root.get("design")), req.getDesign().toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(req.getCategory()) && !"all".equalsIgnoreCase(req.getCategory())) {
			predicates.add(cb.equal(cb.lower(root.get("category")), req.getCategory().toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(req.getParameter()) && !"all".equalsIgnoreCase(req.getParameter())) {
			predicates.add(cb.equal(cb.lower(root.get("parameter")), req.getParameter().toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		return predicates;
	}

	private DesignReceiptsMaster convertToReceiptModel(DesignReceiptsMaster designReceiptsMaster) {
		DesignReceiptsMaster model = designReceiptsMaster;
		if (Strings.isNullOrEmpty(designReceiptsMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (designReceiptsMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (designReceiptsMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (designReceiptsMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(designReceiptsMaster.getAuthStatus());
		}
		return model;
	}

	private DesginReceiptFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<DesignReceiptsMaster> data, PageDetails pageDetails) {
		DesginReceiptFetchResponse response = new DesginReceiptFetchResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}
}
