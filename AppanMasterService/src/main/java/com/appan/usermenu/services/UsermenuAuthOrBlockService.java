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
import com.appan.usermenu.model.AuthOrBlockUsermenuRequest;
import com.google.gson.GsonBuilder;

import jakarta.validation.Valid;

@Service
public class UsermenuAuthOrBlockService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private UserMenuRepository menuRepository;

	private static final Logger logger = LoggerFactory.getLogger(UsermenuAuthOrBlockService.class);

	public CommonResponse authorblockUsermenu(@Valid AuthOrBlockUsermenuRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK Usermenu *************************************");
		CommonResponse response = new CommonResponse();

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserMenu ms = menuRepository.findByUserProfileAndUserRole(req.getUserProfile().toUpperCase(),
					req.getUserRole().toUpperCase());

			if (ms == null) {
				response.setStatus(false);
				response.setMessage("User menu entry not found.");
				response.setRespCode("02");
				return response;
			}

			// Check if UserMenu is already authorized
			if ("1".equalsIgnoreCase(req.getStatus())
					&& "1".equalsIgnoreCase(ms.getAuthStatus() != null ? ms.getAuthStatus() : "")) {
				response.setStatus(false);
				response.setMessage("Usermenu already authorized.");
				response.setRespCode("03");
				return response;
			}

			// Check if UserMenu is already blocked
			if ("3".equalsIgnoreCase(req.getStatus())
					&& "3".equalsIgnoreCase(ms.getAuthStatus() != null ? ms.getAuthStatus() : "")) {
				response.setStatus(false);
				response.setMessage("Usermenu already blocked.");
				response.setRespCode("04");
				return response;
			}

			// Update authorization or block status based on request
			ms.setAuthBy(req.getUsername().toUpperCase());
			ms.setAuthDt(new Date());

			if ("1".equalsIgnoreCase(req.getStatus())) {
				ms.setAuthStatus("1");
				response.setMessage("Usermenu " + ErrorMessages.AUTH_SUCCESS);
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				ms.setAuthStatus("3");
				response.setMessage("Usermenu " + ErrorMessages.BLOCK_SUCCESS);
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage(ErrorMessages.INVALID_STATUS);
				return response;
			}

			menuRepository.save(ms);
			response.setStatus(true);
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
		}
		return response;
	}
}
