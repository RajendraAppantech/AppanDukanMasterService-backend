package com.appan.apimaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.AuthOrBlockAndroidSettingMasterReq;
import com.appan.apimaster.repositories.ApiRepositories.AndroidSettingMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.AndroidSettingMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AndroidSettingMasterAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private AndroidSettingMasterRepository androidSettingMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(AndroidSettingMasterAuthOrBlockService.class);

	public CommonResponse authorBlockAndroidSetting(AuthOrBlockAndroidSettingMasterReq req) {
		CommonResponse response = new CommonResponse();

		try {

			// Step 1: Check username for authentication
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the ID exists in the table
			AndroidSettingMaster setting = androidSettingMasterRepository.findById(req.getId()).orElse(null);
			if (setting == null) {
				response.setStatus(false);
				response.setMessage("Android Setting with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			// Step 3: Handle author/block logic based on status
			if (req.getStatus().equalsIgnoreCase("1") && setting.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Android setting is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && setting.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Android setting is already blocked.");
				response.setRespCode("01");
				return response;
			}

			// Set the authBy and authDate fields for audit purposes
			setting.setAuthBy(req.getUsername().toUpperCase());
			setting.setAuthDate(new Date());

			// Modify the authStatus based on the status value
			if (req.getStatus().equalsIgnoreCase("1")) {
				setting.setAuthStatus("1"); // Authorized
				response.setMessage("Android setting authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				setting.setAuthStatus("3"); // Blocked
				response.setMessage("Android setting blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			// Save the modified AndroidSettingMaster entity
			androidSettingMasterRepository.save(setting);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
