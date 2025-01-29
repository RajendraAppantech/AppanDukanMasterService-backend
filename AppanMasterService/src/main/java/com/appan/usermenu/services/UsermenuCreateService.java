package com.appan.usermenu.services;

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
import com.appan.usermenu.model.CreateUsermenuRequest;
import com.google.gson.GsonBuilder;

import jakarta.validation.Valid;

@Service
public class UsermenuCreateService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private UserMenuRepository menuRepository;

	private static final Logger logger = LoggerFactory.getLogger(UsermenuCreateService.class);

	public CommonResponse createUsermenu(@Valid CreateUsermenuRequest req) {

		logger.info("\r\n\r\n**************************** CREATE USERMENU *************************************");

		CommonResponse response = new CommonResponse();

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			boolean exists = menuRepository.existsByUserProfileAndUserRole(req.getUserProfile().toUpperCase(),
					req.getUserRole().toUpperCase());
			if (exists) {
				response.setStatus(false);
				response.setMessage("User profile and role combination already exists.");
				response.setRespCode("02");
				return response;
			}

			UserMenu newUserMenu = new UserMenu();
			newUserMenu.setUserProfile(req.getUserProfile().toUpperCase());
			newUserMenu.setUserRole(req.getUserRole().toUpperCase());
			newUserMenu.setMenu("100000000000000000000000000000000000000000000000000");
			newUserMenu.setStatus("ACTIVE");
			newUserMenu.setAuthStatus("4");
			newUserMenu.setCreatedBy(req.getUsername().toUpperCase());
			newUserMenu.setCreatedDt(new Date());
			newUserMenu.setRoleName(req.getRoleName());
			
			newUserMenu.setUserType(req.getUserType());
			newUserMenu.setCode(req.getCode());
			
			menuRepository.save(newUserMenu);

			response.setStatus(true);
			response.setMessage("User menu created successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
		}

		return response;
	}
}
