
package com.appan.taxmaster.services;

import java.math.BigDecimal;
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
import com.appan.entity.TaxMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TaxMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.taxmaster.model.FetchTaxRequest;
import com.appan.taxmaster.model.FetchTaxResponse;
import com.appan.taxmaster.model.TaxMasterModel;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchTaxService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchTaxService.class);

	@Autowired
	private TaxMasterRepository taxMasterRepository;

	public FetchTaxResponse fetch(FetchTaxRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH TAX DETAILS *************************************");
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<TaxMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getType(), req.getTaxTypeName(),
							req.getPrimaryTax(), req.getSecondaryTax(), req.getFromDate(), req.getToDate());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (Exception e) {
					e.printStackTrace();
					Logger.error("Exception while building predicates: ", e);
					return null;
				}
			};

			Page<TaxMaster> taxPage = taxMasterRepository.findAll(specification, paging);

			if (taxPage.isEmpty()) {
				return buildErrorResponse(false, "No tax records found.", "01", null, null);
			}

			Logger.info("taxPage: " + taxPage);
			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(taxPage.getNumber() + 1);
			pageDetails.setTotalRecords(taxPage.getTotalElements());
			pageDetails.setNoOfPages(taxPage.getTotalPages());
			pageDetails.setPageSize(taxPage.getSize());

			List<TaxMasterModel> taxData = taxPage.stream().map(this::convertToTaxMasterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", taxData, pageDetails);

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("Exception in fetch service: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<TaxMaster> root, String type, String taxTypeName,
			BigDecimal primaryTax, BigDecimal secondaryTax, String fromDate, String toDate) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		// Filter by fromDate and toDate
		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
			Date startDate = dateFormat.parse(fromDate);
			Date endDate = dateFormat.parse(toDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (!Strings.isNullOrEmpty(taxTypeName) && !"all".equalsIgnoreCase(taxTypeName)) {
			predicates.add(cb.like(cb.lower(root.get("taxTypeName")), "%" + taxTypeName.toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(type) && !"all".equalsIgnoreCase(type)) {
			predicates.add(cb.like(cb.lower(root.get("type")), "%" + type.toLowerCase() + "%"));
		}

		if (primaryTax != null) {
			predicates.add(cb.equal(root.get("primaryTax"), primaryTax));
		}

		if (secondaryTax != null) {
			predicates.add(cb.equal(root.get("secondaryTax"), secondaryTax));
		}

		return predicates;
	}

	private TaxMasterModel convertToTaxMasterModel(TaxMaster tax) {
		TaxMasterModel model = new TaxMasterModel();
		model.setId(tax.getId());
		model.setType(tax.getType());
		model.setTaxTypeName(tax.getTaxTypeName());
		model.setPrimaryTax(tax.getPrimaryTax());
		model.setSecondaryTax(tax.getSecondaryTax());
		model.setCreatedBy(tax.getCreatedBy());
		model.setCreatedDt(tax.getCreatedDt());
		model.setModifyBy(tax.getModifyBy());
		model.setModifyDt(tax.getModifyDt());
		model.setAuthBy(tax.getAuthBy());
		model.setAuthDate(tax.getAuthDate());
		model.setAuthStatus(tax.getAuthStatus());

		return model;
	}

	private FetchTaxResponse buildErrorResponse(boolean status, String message, String respCode,
			List<TaxMasterModel> data, PageDetails pageDetails) {
		FetchTaxResponse response = new FetchTaxResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchTaxResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL TAX DATA *************************************");
		FetchTaxResponse response = new FetchTaxResponse();
		try {

			List<TaxMaster> taxList = taxMasterRepository.findAll();
			if (taxList.isEmpty()) {
				return buildErrorResponse(false, "No tax records found.", "01", null, null);
			}

			List<TaxMasterModel> taxData = taxList.stream().map(this::convertToTaxMasterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", taxData, null);

		} catch (Exception e) {
			Logger.error("Exception in getAllData service: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}

