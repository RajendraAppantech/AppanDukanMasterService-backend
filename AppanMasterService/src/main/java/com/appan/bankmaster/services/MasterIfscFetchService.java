package com.appan.bankmaster.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.entity.MasterIfsc;
import com.appan.bankmaster.masterifsc.model.FetchMasterIfscModel;
import com.appan.bankmaster.masterifsc.model.FetchMasterIfscResponse;
import com.appan.bankmaster.masterifsc.model.MasterIfscFetchRequest;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.MasterIfscRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class MasterIfscFetchService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private MasterIfscRepository masterIfscRepository;

	public FetchMasterIfscResponse fetchMasterIfsc(MasterIfscFetchRequest req, Integer pageNo, Integer pageSize) {

		UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
		if (master == null) {
			return buildResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
		}

		Pageable paging = PageRequest.of(pageNo - 1, pageSize);

		Specification<MasterIfsc> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
			query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};

		Page<MasterIfsc> pageResult = masterIfscRepository.findAll(specification, paging);
		if (pageResult.isEmpty()) {
			return buildResponse(false, "IFSC details not found.", "01", null, null);
		}

		List<FetchMasterIfscModel> models = pageResult.stream().map(this::convertToResponseModel)
				.collect(Collectors.toList());

		PageDetails pageDetails = new PageDetails();
		pageDetails.setPageNo(pageResult.getNumber() + 1);
		pageDetails.setTotalRecords(pageResult.getTotalElements());
		pageDetails.setNoOfPages(pageResult.getTotalPages());
		pageDetails.setPageSize(pageResult.getSize());

		return buildResponse(true, "SUCCESS", "00", models, pageDetails);
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<MasterIfsc> root, MasterIfscFetchRequest req) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		try {
			if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())
					&& !"all".equalsIgnoreCase(req.getFromDate()) && !"all".equalsIgnoreCase(req.getToDate())) {

				Date fromDate = dateFormat.parse(req.getFromDate());
				Date toDate = dateFormat.parse(req.getToDate());

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				toDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), fromDate, toDate));
			}
		} catch (ParseException e) {
			throw new RuntimeException("Invalid date format in request");
		}

		if (!Strings.isNullOrEmpty(req.getBankName()) && !"all".equalsIgnoreCase(req.getBankName())) {
			predicates.add(cb.like(cb.lower(root.get("bankName")), "%" + req.getBankName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getIfscCode()) && !"all".equalsIgnoreCase(req.getIfscCode())) {
			predicates.add(cb.equal(cb.lower(root.get("ifscCode")), req.getIfscCode().toLowerCase()));
		}

		if (req.getPriority() != null && !"all".equalsIgnoreCase(String.valueOf(req.getPriority()))) {
			predicates.add(cb.equal(root.get("priority"), req.getPriority()));
		}

		return predicates;
	}

	private FetchMasterIfscModel convertToResponseModel(MasterIfsc entity) {
		FetchMasterIfscModel model = new FetchMasterIfscModel();
		model.setId(entity.getId());
		model.setBankName(entity.getBankName());
		model.setBankCode(entity.getBankCode());
		model.setIfscCode(entity.getIfscCode());
		model.setAeps(entity.getAeps());
		model.setDmtm(entity.getDmtm());
		model.setDmte(entity.getDmte());
		model.setDmtb(entity.getDmtb());
		model.setIsCreditCard(entity.getIsCreditCard());
		model.setPriority(entity.getPriority());
		model.setIsSettlement(entity.getIsSettlement());
		model.setFile(entity.getFile());
		model.setCreatedBy(entity.getCreatedBy());
		model.setCreatedDt(entity.getCreatedDt());
		model.setModifyBy(entity.getModifyBy());
		model.setModifyDt(entity.getModifyDt());
		model.setAuthBy(entity.getAuthBy());
		model.setAuthDate(entity.getAuthDate());
		if (Strings.isNullOrEmpty(entity.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (entity.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (entity.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (entity.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(entity.getAuthStatus());
		}
		return model;
	}

	private FetchMasterIfscResponse buildResponse(boolean status, String message, String respCode,
			List<FetchMasterIfscModel> data, PageDetails pageDetails) {
		FetchMasterIfscResponse response = new FetchMasterIfscResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchMasterIfscResponse getAllMasterIfsc() {
		FetchMasterIfscResponse response = new FetchMasterIfscResponse();
		try {
			List<MasterIfsc> masterIfscList = masterIfscRepository.findAll();

			if (masterIfscList.isEmpty()) {
				return buildResponse(false, "Master IFSC details not found.", "01", null, null);
			}

			List<FetchMasterIfscModel> masterIfscModels = masterIfscList.stream().map(this::convertToResponseModel)
					.collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", masterIfscModels, null);
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
