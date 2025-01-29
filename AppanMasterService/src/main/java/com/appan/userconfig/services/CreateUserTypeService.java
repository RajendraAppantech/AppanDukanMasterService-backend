package com.appan.userconfig.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UserMenu;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.userconfig.models.CreateUserTypeRequest;

import jakarta.validation.Valid;

@Service
public class CreateUserTypeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserMenuRepository userMenuRepository;

	private static final Logger logger = LoggerFactory.getLogger(CreateUserTypeService.class);

	public CommonResponse create(@Valid CreateUserTypeRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserMenu existingMenu = userMenuRepository.findByUserProfileAndUserRole(req.getUserProfile(),
					req.getUserRole());
			if (existingMenu != null) {
				response.setStatus(false);
				response.setMessage("User menu entry already exists for the given profile and role");
				response.setRespCode("01");
				return response;
			}

			UserMenu newMenu = new UserMenu();
			newMenu.setRoleName(req.getRoleName());
			newMenu.setUserProfile(req.getUserProfile());
			newMenu.setUserRole(req.getUserRole());
			newMenu.setMenu(
					"000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
			newMenu.setRank(req.getRank());
			newMenu.setIsUser(req.getIsUser());
			newMenu.setIsAllow(req.getIsAllow());
			newMenu.setIsCommission(req.getIsCommission());
			newMenu.setStatus(req.getStatus());
			newMenu.setCreatedBy(req.getUsername().toUpperCase());
			newMenu.setCreatedDt(new Date());
			newMenu.setAuthStatus("4");

			try {
				userMenuRepository.save(newMenu);
			} catch (Exception e) {
				logger.error("Error while saving user menu entry: ", e);
				response.setStatus(false);
				response.setMessage("An error occurred while saving the user menu entry.");
				response.setRespCode("02");
				return response;
			}

			response.setStatus(true);
			response.setMessage("User menu entry created successfully");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Exception in create user type service: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
