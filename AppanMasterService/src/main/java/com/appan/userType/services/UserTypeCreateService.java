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
import com.appan.userType.models.UserTypeCreateRequest;

import jakarta.validation.Valid;

@Service
public class UserTypeCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserTypeMasterRepository userTypeMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserTypeCreateService.class);

	public CommonResponse create(@Valid UserTypeCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			boolean exists = userTypeMasterRepository.existsByUserTypeAndUserCodeIgnoreCase(req.getUserType().trim(), req.getUserCode().trim());
			if (exists) {
			    response.setStatus(false);
			    response.setMessage("UserType with the same userType and userCode already exists.");
			    response.setRespCode("01");
			    return response;
			}


			UserTypeMaster userTypeMaster = new UserTypeMaster();
			userTypeMaster.setUserType(req.getUserType());
			userTypeMaster.setUserRank(req.getUserRank());
			userTypeMaster.setUserCode(req.getUserCode());
			userTypeMaster.setDescription(req.getDescription());
			userTypeMaster.setIsUser(req.getIsUser());
			userTypeMaster.setIsAllow(req.getIsAllow());
			userTypeMaster.setIsCommission(req.getIsCommission());
			userTypeMaster.setStatus(req.getStatus());
			userTypeMaster.setCreatedBy(req.getUsername().toUpperCase());
			userTypeMaster.setCreatedDate(new Date());
			userTypeMaster.setAuthStatus("4");

			userTypeMasterRepository.save(userTypeMaster);

			response.setStatus(true);
			response.setMessage("UserType created successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating user type: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
