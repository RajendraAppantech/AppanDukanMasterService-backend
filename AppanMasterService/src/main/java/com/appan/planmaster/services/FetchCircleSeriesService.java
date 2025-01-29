package com.appan.planmaster.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.appan.planmaster.circleseries.model.CircleSeriesModel;
import com.appan.planmaster.circleseries.model.FetchCircleSeriesRequest;
import com.appan.planmaster.circleseries.model.FetchCircleSeriesResponse;
import com.appan.planmaster.entity.CircleSeriesMaster;
import com.appan.planmaster.repositories.PlanMasterRepositories.CircleSeriesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchCircleSeriesService {
	
	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchCircleSeriesService.class);

	@Autowired
	private CircleSeriesMasterRepository circleSeriesMasterRepository;

	public FetchCircleSeriesResponse fetch(FetchCircleSeriesRequest req, Integer pageNo, Integer pageSize) {
		logger.info("**************************** FETCH CIRCLE SERIES *************************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<CircleSeriesMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<CircleSeriesMaster> pageResults = circleSeriesMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Circle Series not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<CircleSeriesModel> circleSeriesData = pageResults.stream().map(this::convertToCircleSeriesModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", circleSeriesData, pageDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<CircleSeriesMaster> root,
			FetchCircleSeriesRequest req) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (req.getNumberPrefix() != null && !req.getNumberPrefix().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("numberPrefix")), req.getNumberPrefix().toLowerCase()));
		}
		if (req.getCircleName() != null && !req.getCircleName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("circleName")), req.getCircleName().toLowerCase()));
		}
		if (req.getOperatorName() != null && !req.getOperatorName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("operatorName")), req.getOperatorName().toLowerCase()));
		}
		if (req.getStatus() != null && !req.getStatus().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		// Date filters
		if (req.getFromDate() != null && req.getToDate() != null) {
			try {
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());

				endDate = new Date(endDate.getTime() + 86400000L); 
				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (Exception e) {
				logger.error("Error parsing date filters", e);
			}
		}

		return predicates;
	}

	private CircleSeriesModel convertToCircleSeriesModel(CircleSeriesMaster circleSeriesMaster) {
		CircleSeriesModel model = new CircleSeriesModel();
		model.setId(circleSeriesMaster.getId());
		model.setNumberPrefix(circleSeriesMaster.getNumberPrefix());
		model.setCircleName(circleSeriesMaster.getCircleName());
		model.setOperatorName(circleSeriesMaster.getOperatorName());
		model.setStatus(circleSeriesMaster.getStatus());
		model.setCreatedBy(circleSeriesMaster.getCreatedBy());
		model.setCreatedDt(circleSeriesMaster.getCreatedDt());
		model.setModifyBy(circleSeriesMaster.getModifyBy());
		model.setModifyDt(circleSeriesMaster.getModifyDt());
		model.setAuthBy(circleSeriesMaster.getAuthBy());
		model.setAuthDate(circleSeriesMaster.getAuthDate());
		model.setAuthStatus(circleSeriesMaster.getAuthStatus());
		model.setIsUpdate(circleSeriesMaster.getIsUpdate());
		model.setIsCalledForUpdate(circleSeriesMaster.getIsCalledForUpdate());
		return model;
	}

	private FetchCircleSeriesResponse buildErrorResponse(boolean status, String message, String respCode,
			List<CircleSeriesModel> data, PageDetails pageDetails) {
		FetchCircleSeriesResponse response = new FetchCircleSeriesResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchCircleSeriesResponse getAllData() {
		logger.info("**************************** GET ALL CIRCLE SERIES *************************************");

		FetchCircleSeriesResponse response = new FetchCircleSeriesResponse();
		try {
			List<CircleSeriesMaster> allSeries = circleSeriesMasterRepository.findByStatus("Active");
			if (allSeries.isEmpty()) {
				return buildErrorResponse(false, "No Circle Series found.", "01", null, null);
			}

			List<CircleSeriesModel> circleSeriesData = allSeries.stream().map(this::convertToCircleSeriesModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", circleSeriesData, null);
		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
