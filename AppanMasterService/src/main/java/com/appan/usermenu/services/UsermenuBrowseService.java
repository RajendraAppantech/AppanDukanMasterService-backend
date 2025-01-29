package com.appan.usermenu.services;

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
import com.appan.entity.UserMenu;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.usermenu.model.FetchUsermenuResponse;
import com.appan.usermenu.model.UserMenuModel;
import com.appan.usermenu.model.UsermenuRequest;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UsermenuBrowseService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private UserMenuRepository menuRepository;

	private static final Logger logger = LoggerFactory.getLogger(UsermenuBrowseService.class);

	public FetchUsermenuResponse fetchUsermenu(UsermenuRequest req, Integer pageNo, Integer pageSize) {
		logger.info(
				"\r\n\r\n**************************** FETCH USERMENU DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<UserMenu> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getFromDate(), req.getToDate(),
							req.getUserType());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (ParseException e) {
					logger.error("Error parsing dates: ", e);
					return null;
				}
			};

			Page<UserMenu> menuPage = menuRepository.findAll(specification, paging);
			if (menuPage.isEmpty()) {
				return buildErrorResponse(false, "User Menu " + ErrorMessages.NOT_FOUND, "01", null, null);
			}

			PageDetails details = new PageDetails();
			details.setPageNo(menuPage.getNumber() + 1);
			details.setTotalRecords(menuPage.getTotalElements());
			details.setNoOfPages(menuPage.getTotalPages());
			details.setPageSize(menuPage.getSize());

			List<UserMenuModel> userMenus = menuPage.stream().map(this::convertToModel).collect(Collectors.toList());
			return buildErrorResponse(true, ErrorMessages.SUCCESS, "00", userMenus, details);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			return buildErrorResponse(false, ErrorMessages.EXCEPTION, "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<UserMenu> root, String fromDate, String toDate,
			String userType) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
			Date startDate = dateFormat.parse(fromDate);
			Date endDate = dateFormat.parse(toDate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), cb.literal(startDate), cb.literal(endDate)));
		}

		if (!Strings.isNullOrEmpty(userType)) {
			predicates.add(cb.equal(cb.lower(root.get("roleName")), userType.toLowerCase()));
		}

		return predicates;
	}

	private UserMenuModel convertToModel(UserMenu menu) {
		UserMenuModel model = new UserMenuModel();
		model.setMenuId(menu.getMenuId());
		model.setUserProfile(menu.getUserProfile());
		model.setUserRole(menu.getUserRole());
		model.setMenu(menu.getMenu());
		model.setStatus(menu.getStatus());
		model.setCreatedBy(menu.getCreatedBy());
		model.setCreatedDt(menu.getCreatedDt());
		model.setModifyBy(menu.getModifyBy());
		model.setModifyDt(menu.getModifyDt());
		model.setAuthBy(menu.getAuthBy());
		model.setAuthDt(menu.getAuthDt());
		model.setRoleName(menu.getRoleName());

		model.setUserType(menu.getUserType());
		model.setCode(menu.getCode());

		if ("1".equals(menu.getAuthStatus())) {
			model.setAuthStatus("ACTIVE");
		} else if ("3".equals(menu.getAuthStatus())) {
			model.setAuthStatus("BLOCK");
		} else if ("4".equals(menu.getAuthStatus())) {
			model.setAuthStatus("IN-ACTIVE");
		} else if (Strings.isNullOrEmpty(menu.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else {
			model.setAuthStatus(menu.getAuthStatus());
		}

		return model;
	}

	private FetchUsermenuResponse buildErrorResponse(boolean status, String message, String respCode,
			List<UserMenuModel> data, PageDetails pageDetails) {
		FetchUsermenuResponse response = new FetchUsermenuResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}
}
