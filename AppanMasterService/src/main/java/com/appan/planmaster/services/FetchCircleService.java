package com.appan.planmaster.services;

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
import com.appan.planmaster.circle.model.CircleMasterModel;
import com.appan.planmaster.circle.model.FetchCircleRequest;
import com.appan.planmaster.circle.model.FetchCircleResponse;
import com.appan.planmaster.entity.CirclesMaster;
import com.appan.planmaster.repositories.PlanMasterRepositories.CirclesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchCircleService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchCircleService.class);

	@Autowired
	private CirclesMasterRepository circlesMasterRepository;

	public FetchCircleResponse fetch(FetchCircleRequest req, Integer pageNo, Integer pageSize) {
		logger.info("\r\n\r\n**************************** FETCH CIRCLE DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<CirclesMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getCircleName(), req.getCode(),
						req.getCircleCode1(), req.getCircleCode2(), req.getCircleCode3(), req.getStatus(),
						req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<CirclesMaster> pageResults = circlesMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Circle not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<CircleMasterModel> circleData = pageResults.stream().map(this::convertToCircleModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", circleData, pageDetails);
		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<CirclesMaster> root, String circleName,
			String code, String circleCode1, String circleCode2, String circleCode3, String status, String fromDate,
			String toDate) {

		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
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

		if (!Strings.isNullOrEmpty(circleName) && !"all".equalsIgnoreCase(circleName)) {
			predicates.add(cb.equal(cb.lower(root.get("circleName")), circleName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(code) && !"all".equalsIgnoreCase(code)) {
			predicates.add(cb.equal(cb.lower(root.get("code")), code.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(circleCode1) && !"all".equalsIgnoreCase(circleCode1)) {
			predicates.add(cb.equal(cb.lower(root.get("circleCode1")), circleCode1.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(circleCode2) && !"all".equalsIgnoreCase(circleCode2)) {
			predicates.add(cb.equal(cb.lower(root.get("circleCode2")), circleCode2.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(circleCode3) && !"all".equalsIgnoreCase(circleCode3)) {
			predicates.add(cb.equal(cb.lower(root.get("circleCode3")), circleCode3.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(status) && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private CircleMasterModel convertToCircleModel(CirclesMaster circleMaster) {
		CircleMasterModel model = new CircleMasterModel();
		model.setId(circleMaster.getId());
		model.setCircleName(circleMaster.getCircleName());
		model.setCode(circleMaster.getCode());
		model.setCircleCode1(circleMaster.getCircleCode1());
		model.setCircleCode2(circleMaster.getCircleCode2());
		model.setCircleCode3(circleMaster.getCircleCode3());
		model.setStatus(circleMaster.getStatus());
		model.setCreatedBy(circleMaster.getCreatedBy());
		model.setCreatedDt(circleMaster.getCreatedDt());
		model.setModifyBy(circleMaster.getModifyBy());
		model.setModifyDt(circleMaster.getModifyDt());
		model.setAuthBy(circleMaster.getAuthBy());
		model.setAuthDate(circleMaster.getAuthDate());
		model.setAuthStatus(circleMaster.getAuthStatus());

		return model;
	}

	private FetchCircleResponse buildErrorResponse(boolean status, String message, String respCode,
			List<CircleMasterModel> data, PageDetails pageDetails) {
		FetchCircleResponse response = new FetchCircleResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchCircleResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL CIRCLE CODE *************************************");

		FetchCircleResponse response = new FetchCircleResponse();
		try {
			List<CirclesMaster> allCircles = circlesMasterRepository.findByStatus("Active");
			if (allCircles.isEmpty()) {
				return buildErrorResponse(false, "No circles found.", "01", null, null);
			}

			List<CircleMasterModel> circleData = allCircles.stream().map(this::convertToCircleModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", circleData, null);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
