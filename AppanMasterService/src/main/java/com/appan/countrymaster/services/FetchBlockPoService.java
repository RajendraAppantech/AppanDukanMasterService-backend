package com.appan.countrymaster.services;

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
import com.appan.countrymaster.blockpo.models.BlockPoMasterModel;
import com.appan.countrymaster.blockpo.models.FetchBlockPoRequest;
import com.appan.countrymaster.blockpo.models.FetchBlockPoResponse;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.BlockPoMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BlockPoMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchBlockPoService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private BlockPoMasteRepository blockPoMasteRepository;
	private static final Logger logger = LoggerFactory.getLogger(FetchBlockPoService.class);

	public FetchBlockPoResponse fetch(FetchBlockPoRequest req, Integer pageNo, Integer pageSize) {
		logger.info("**************************** FETCH BLOCK/PO DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<BlockPoMaster> specification = createSpecification(req);

			Page<BlockPoMaster> pageResult = blockPoMasteRepository.findAll(specification, paging);

			if (pageResult.isEmpty()) {
				return buildResponse(false, "Block/PO details not found.", "01", null, null);
			}

			List<BlockPoMasterModel> blockPoModels = pageResult.stream().map(this::convertToBlockPoMasterModel)
					.collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResult.getNumber() + 1);
			pageDetails.setTotalRecords(pageResult.getTotalElements());
			pageDetails.setNoOfPages(pageResult.getTotalPages());
			pageDetails.setPageSize(pageResult.getSize());

			return buildResponse(true, "SUCCESS", "00", blockPoModels, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			return buildResponse(false, "Exception occurred", "03", null, null);
		}
	}

	private Specification<BlockPoMaster> createSpecification(FetchBlockPoRequest req) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			try {
				buildPredicates(criteriaBuilder, root, req.getBlockPoName(), req.getCityName(), req.getStatus(),
						req.getFromDate(), req.getToDate(), predicates);
			} catch (ParseException e) {
				logger.error("Date parsing error: ", e);
			}
			query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

	private void buildPredicates(CriteriaBuilder cb, Root<BlockPoMaster> root, String blockPoName, String cityName,
			String authStatus, String fromDate, String toDate, List<Predicate> predicates) throws ParseException {

		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
			Date startDate = DateUtils.parseDate(fromDate);
			Date endDate = DateUtils.parseDate(toDate);

			// Include the end date by adding one day
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (!Strings.isNullOrEmpty(blockPoName) && !"all".equalsIgnoreCase(blockPoName)) {
			predicates.add(cb.equal(cb.upper(root.get("blockPoName")), blockPoName.toUpperCase()));
		}

		if (!Strings.isNullOrEmpty(cityName) && !"all".equalsIgnoreCase(cityName)) {
			predicates.add(cb.equal(cb.upper(root.get("cityName")), cityName.toUpperCase()));
		}

		if (!Strings.isNullOrEmpty(authStatus) && !"all".equalsIgnoreCase(authStatus)) {
			predicates.add(cb.equal(cb.upper(root.get("authStatus")), authStatus.toUpperCase()));
		}
	}

	private BlockPoMasterModel convertToBlockPoMasterModel(BlockPoMaster model) {
		BlockPoMasterModel blockPoModel = new BlockPoMasterModel();
		blockPoModel.setBlockPoId(model.getBlockPoId());
		blockPoModel.setBlockPoName(model.getBlockPoName());
		blockPoModel.setCityName(model.getCityName());
		blockPoModel.setStatus(model.getStatus());
		blockPoModel.setCreatedDt(model.getCreatedDt());
		blockPoModel.setCreatedBy(model.getCreatedBy());
		blockPoModel.setModifyBy(model.getModifyBy());
		blockPoModel.setModifyDt(model.getModifyDt());
		blockPoModel.setAuthBy(model.getAuthBy());
		blockPoModel.setAuthDate(model.getAuthDate());
		blockPoModel.setAuthStatus(resolveAuthStatus(model.getAuthStatus()));
		return blockPoModel;
	}

	private String resolveAuthStatus(String authStatus) {
		if (Strings.isNullOrEmpty(authStatus)) {
			return "UNKNOWN";
		}
		switch (authStatus) {
		case "1":
			return "ACTIVE";
		case "3":
			return "BLOCK";
		case "4":
			return "IN-ACTIVE";
		default:
			return authStatus;
		}
	}

	private FetchBlockPoResponse buildResponse(boolean status, String message, String respCode,
			List<BlockPoMasterModel> data, PageDetails pageDetails) {
		FetchBlockPoResponse response = new FetchBlockPoResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchBlockPoResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL Block/Po Data *************************************");
		FetchBlockPoResponse response = new FetchBlockPoResponse();
		try {

			List<BlockPoMaster> blockPoList = blockPoMasteRepository.findByStatus("Active");
			if (blockPoList.isEmpty()) {
				return buildResponse(false, "Block/PO details not found.", "01", null, null);
			}

			List<BlockPoMasterModel> blockPoModels = blockPoList.stream().map(this::convertToBlockPoMasterModel)
					.collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", blockPoModels, null);

		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}

	// Inner DateUtils class
	public static class DateUtils {
		public static Date parseDate(String dateStr) throws ParseException {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			return dateFormat.parse(dateStr);
		}
	}
}
