package com.appan.serviceConfig.service.services;

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
import com.appan.entity.ServiceConfigServiceMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigServiceRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.service.model.ServiceFetchRequest;
import com.appan.serviceConfig.service.model.ServiceFetchResponse;
import com.google.common.base.Strings;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ServiceFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigServiceRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(ServiceFetchService.class);

	public ServiceFetchResponse fetch(ServiceFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<ServiceConfigServiceMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getServiceName(), req.getServiceCode(), req.getServiceType() , req.getCategoryName(),
					 req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<ServiceConfigServiceMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Service not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<ServiceConfigServiceMaster> ServiceData = pageResults.stream().map(this::convertToServiceModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", ServiceData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ServiceConfigServiceMaster> root, String ServiceName, String ServiceCode, 
			String serviceType, String categoryName, String status, String fromDate, String toDate) {
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

		if (!Strings.isNullOrEmpty(ServiceName)  && !"all".equalsIgnoreCase(ServiceName)) {
			predicates.add(cb.equal(cb.lower(root.get("serviceName")), ServiceName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(ServiceCode) && !"all".equalsIgnoreCase(ServiceCode)) {
			predicates.add(cb.equal(cb.lower(root.get("serviceCode")), ServiceCode.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(serviceType) && !"all".equalsIgnoreCase(serviceType)) {
			predicates.add(cb.equal(cb.lower(root.get("serviceType")), serviceType.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(categoryName) && !"all".equalsIgnoreCase(categoryName)) {
			predicates.add(cb.equal(cb.lower(root.get("categoryName")), categoryName.toLowerCase()));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private ServiceConfigServiceMaster convertToServiceModel(ServiceConfigServiceMaster ServiceConfigServiceMaster) {
		ServiceConfigServiceMaster model = ServiceConfigServiceMaster;
		if (Strings.isNullOrEmpty(ServiceConfigServiceMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (ServiceConfigServiceMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (ServiceConfigServiceMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (ServiceConfigServiceMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(ServiceConfigServiceMaster.getAuthStatus());
		}
		return model;
	}

	private ServiceFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<ServiceConfigServiceMaster> data, PageDetails pageDetails) {
		ServiceFetchResponse response = new ServiceFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public ServiceFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL Service  *************************************");

		ServiceFetchResponse response = new ServiceFetchResponse();
		try {
			List<ServiceConfigServiceMaster> allService = repository.findByStatus("Active");
			if (allService.isEmpty()) {
				return buildErrorResponse(false, "No Service found.", "01", null, null);
			}

			List<ServiceConfigServiceMaster> ServiceData = allService.stream().map(this::convertToServiceModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", ServiceData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
