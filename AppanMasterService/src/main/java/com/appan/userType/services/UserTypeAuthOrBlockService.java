package com.appan.userType.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UserTypeMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserTypeMasterRepository;
import com.appan.userType.models.UserTypeAuthOrBlockRequest;

import jakarta.validation.Valid;

@Service
public class UserTypeAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserTypeMasterRepository userTypeMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserTypeAuthOrBlockService.class);

	public CommonResponse authorblock(@Valid UserTypeAuthOrBlockRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserTypeMaster menuItem = userTypeMasterRepository.findById(req.getMenuId()).orElse(null);
			if (menuItem == null) {
				response.setStatus(false);
				response.setMessage("Menu item with the given ID not found.");
				response.setRespCode("01");
				return response;
			}

			String currentStatus = menuItem.getAuthStatus();
			if ("1".equalsIgnoreCase(req.getStatus()) && "1".equalsIgnoreCase(currentStatus)) {
				response.setStatus(false);
				response.setMessage("User type is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if ("3".equalsIgnoreCase(req.getStatus()) && "3".equalsIgnoreCase(currentStatus)) {
				response.setStatus(false);
				response.setMessage("User type is already blocked.");
				response.setRespCode("01");
				return response;
			}

			menuItem.setAuthorizedBy(req.getUsername().toUpperCase());
			menuItem.setAuthorizedDate(new Date());

			if ("1".equalsIgnoreCase(req.getStatus())) {
				menuItem.setAuthStatus("1");
				response.setMessage("User type authorized successfully.");
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				menuItem.setAuthStatus("3");
				response.setMessage("User type blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			userTypeMasterRepository.save(menuItem);
			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
