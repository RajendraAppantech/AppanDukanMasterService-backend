package com.appan.user.services;

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
import com.appan.user.model.UserRequest;
import com.appan.utils.MyUtils;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

@Service
public class CreateUserService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private MyUtils dbUtils;

	@Autowired
	private UserMenuRepository menuRepository;

	private static final Logger Logger = LoggerFactory.getLogger(CreateUserService.class);

	public CommonResponse createUser(UserRequest req) {
		Logger.info("\r\n\r\n**************************** CREATE USER *************************************");
		CommonResponse response = new CommonResponse();
		String menu = null;
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			if (!req.getUserProfile().equalsIgnoreCase("OPERATION")) {
				if (Strings.isNullOrEmpty(req.getUserCode())) {
					response.setStatus(false);
					response.setMessage("User code is required");
					response.setRespCode("01");
					return response;
				}
			}

			CommonResponse resp = dbUtils.validateUserAndMobile(req.getUserId().toUpperCase(), req.getMobileNo());
			if (!resp.isStatus()) {
				response.setStatus(false);
				response.setMessage(resp.getMessage());
				response.setRespCode(resp.getRespCode());
				return response;
			}

			UserMenu userMenu = menuRepository.findByUserRoleAndUserProfile(req.getUserRole(), req.getUserProfile());
			if (userMenu == null) {
				menu = "";
			} else {
				menu = userMenu.getMenu();
			}
			
			UserMaster ms = new UserMaster();
			ms.setUserId(req.getUserId().toUpperCase());
			ms.setName(req.getName().toUpperCase());
			ms.setMobileNo(req.getMobileNo());
			ms.setEmailId(req.getEmailId());
			ms.setUserRole(req.getUserRole().toUpperCase());
			ms.setUserProfile(req.getUserProfile().toUpperCase());
			ms.setStatus("1");
			ms.setUserMenu(menu);
			ms.setUserCode(req.getUserCode());
			ms.setPasswd("0000000000000000000000000000000000000000000000000000000000000000");
			ms.setCreatedBy(master.getUserId().toUpperCase());
			ms.setCreatedDt(new Date());
			ms.setAuthStatus("4");
			masterRepository.save(ms);

			response.setStatus(true);
			response.setMessage("User " + ErrorMessages.CREATE_SUCCESS);
			response.setRespCode("00");
			return response;
		} catch (

		Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
			return response;
		}
	}
}