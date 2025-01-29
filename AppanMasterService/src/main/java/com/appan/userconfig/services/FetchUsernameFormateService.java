package com.appan.userconfig.services;

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
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.entity.UsernameFormat;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UsernameFormatRepository;
import com.appan.userconfig.models.FetchUsernameFormateRequest;
import com.appan.userconfig.models.FetchUsernameFormateResponse;
import com.appan.userconfig.models.UsernameFormateModel;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchUsernameFormateService {

	@Autowired
	private UsernameFormatRepository usernameFormatRepository;

	@Autowired
	private UserMasterRepository masterRepository;

	public FetchUsernameFormateResponse fetch(FetchUsernameFormateRequest req, Integer pageNo, Integer pageSize) {
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<UsernameFormat> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			};

			Page<UsernameFormat> usernamePage = usernameFormatRepository.findAll(specification, paging);

			if (usernamePage.isEmpty()) {
				return buildResponse(false, "No username format records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(usernamePage.getNumber() + 1);
			pageDetails.setTotalRecords(usernamePage.getTotalElements());
			pageDetails.setNoOfPages(usernamePage.getTotalPages());
			pageDetails.setPageSize(usernamePage.getSize());

			List<UsernameFormateModel> usernameData = usernamePage.stream().map(this::convertToModel)
					.collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", usernameData, pageDetails);

		} catch (Exception e) {
			e.printStackTrace();
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<UsernameFormat> root,
			FetchUsernameFormateRequest req) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (req.getUsernameFormat() != null && !req.getUsernameFormat().isEmpty()) {
			predicates.add(
					cb.like(cb.lower(root.get("usernameFormat")), "%" + req.getUsernameFormat().toLowerCase() + "%"));
		}

		if (req.getUserType() != null && !req.getUserType().isEmpty()) {
			predicates.add(cb.like(cb.lower(root.get("userType")), "%" + req.getUserType().toLowerCase() + "%"));
		}

		if (req.getStatus() != null && !req.getStatus().isEmpty()) {
			predicates.add(cb.equal(root.get("status"), req.getStatus()));
		}

		if (req.getFromDate() != null && req.getToDate() != null) {

			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		return predicates;
	}

	private UsernameFormateModel convertToModel(UsernameFormat usernameFormate) {
		UsernameFormateModel model = new UsernameFormateModel();
		model.setId(usernameFormate.getId());
		model.setUsernameFormat(usernameFormate.getUsernameFormat());
		model.setUserType(usernameFormate.getUserType());
		model.setPrefix(usernameFormate.getPrefix());
		model.setSuffix(usernameFormate.getSuffix());
		model.setStatus(usernameFormate.getStatus());
		model.setCreatedBy(usernameFormate.getCreatedBy());
		model.setCreatedDt(usernameFormate.getCreatedDt());
		model.setModifyBy(usernameFormate.getModifyBy());
		model.setModifyDt(usernameFormate.getModifyDt());
		model.setAuthBy(usernameFormate.getAuthBy());
		model.setAuthDt(usernameFormate.getAuthDt());
		model.setAuthStatus(usernameFormate.getAuthStatus());
		return model;
	}

	private FetchUsernameFormateResponse buildResponse(boolean status, String message, String respCode,
			List<UsernameFormateModel> data, PageDetails pageDetails) {
		FetchUsernameFormateResponse response = new FetchUsernameFormateResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchUsernameFormateResponse getAllData() {
		try {
			List<UsernameFormat> usernameList = usernameFormatRepository.findByAuthStatus("1");
			if (usernameList.isEmpty()) {
				return buildResponse(false, "No username format records found.", "01", null, null);
			}

			List<UsernameFormateModel> usernameData = usernameList.stream().map(this::convertToModel)
					.collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", usernameData, null);

		} catch (Exception e) {
			e.printStackTrace();
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}
