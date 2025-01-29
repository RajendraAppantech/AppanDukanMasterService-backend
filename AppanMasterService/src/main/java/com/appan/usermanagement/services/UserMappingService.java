package com.appan.usermanagement.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.usermanagement.models.FromUserData;
import com.appan.usermanagement.models.UserMappingRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;

@Service
public class UserMappingService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserManagementMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(UserMappingService.class);

	public CommonResponse userMapping(UserMappingRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			if (req.getFromUserData() == null) {
				response.setStatus(false);
				response.setMessage("From user details cannot be null");
				response.setRespCode("01");
				return response;
			}

			processMobileNumbers(req);

			response.setStatus(true);
			response.setMessage("User mapping updated successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating wallet: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}
		return response;
	}

	public void processMobileNumbers(UserMappingRequest req) {
		// Split the input string by comma
		for (FromUserData fromUserIdData : req.getFromUserData()) {// FromUserData is a list of userID data

			UserManagementMaster mast = repository.findByUserId(fromUserIdData.getFromUserId());
			if (mast == null) {
				logger.info("User ID not found: ", fromUserIdData.getFromUserId());
			} else {
				mast.setParent(req.getToUserId());
				mast.setModifyBy(req.getUsername().toUpperCase());
				mast.setModifyDt(new Date());
				repository.save(mast);
			}
		}
	}
}