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
import com.appan.userconfig.models.AuthOrBlockUserTypeRequest;

@Service
public class AuthBlockUserTypeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserMenuRepository userMenuRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockUserTypeService.class);

	public CommonResponse authorblock(AuthOrBlockUserTypeRequest req) {
		logger.info("**************************** AUTH OR BLOCK USER TYPE *************************************");

		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserMenu userMenu = userMenuRepository.findById(req.getMenuId()).orElse(null);
			if (userMenu == null) {
				response.setStatus(false);
				response.setMessage("Menu entry with the given ID not found.");
				response.setRespCode("01");
				return response;
			}

			String currentAuthStatus = userMenu.getAuthStatus();
			if ("1".equals(req.getStatus()) && "1".equals(currentAuthStatus)) {
				response.setStatus(false);
				response.setMessage("User is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if ("3".equals(req.getStatus()) && "3".equals(currentAuthStatus)) {
				response.setStatus(false);
				response.setMessage("User is already blocked.");
				response.setRespCode("01");
				return response;
			}

			userMenu.setAuthBy(req.getUsername().toUpperCase());
			userMenu.setAuthDt(new Date());

			if ("1".equals(req.getStatus())) {
				userMenu.setAuthStatus("1");
				response.setMessage("User authorized successfully.");
			} else if ("3".equals(req.getStatus())) {
				userMenu.setAuthStatus("3");
				response.setMessage("User blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid status.");
				return response;
			}

			userMenuRepository.save(userMenu);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Exception in authorblock: ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while processing the request.");
			response.setRespCode("03");
			return response;
		}
	}
}
