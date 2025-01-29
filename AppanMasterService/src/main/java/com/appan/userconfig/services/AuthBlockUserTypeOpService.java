package com.appan.userconfig.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UserTypeOperation;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserTypeOperationRepository;
import com.appan.userconfig.models.AuthOrBlockUserTypeOpRequest;

@Service
public class AuthBlockUserTypeOpService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserTypeOperationRepository userTypeOpRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockUserTypeOpService.class);

	public CommonResponse authorblock(AuthOrBlockUserTypeOpRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK USER TYPE OPERATION *************************************");

		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserTypeOperation userTypeOperation = userTypeOpRepository.findById(req.getId()).orElse(null);
			if (userTypeOperation == null) {
				response.setStatus(false);
				response.setMessage("User Type Operation with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && userTypeOperation.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("User Type Operation is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && userTypeOperation.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("User Type Operation is already blocked.");
				response.setRespCode("01");
				return response;
			}

			userTypeOperation.setAuthBy(req.getUsername().toUpperCase());
			userTypeOperation.setAuthDt(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				userTypeOperation.setAuthStatus("1");
				response.setMessage("User Type Operation authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				userTypeOperation.setAuthStatus("3");
				response.setMessage("User Type Operation blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			userTypeOpRepository.save(userTypeOperation);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION: " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
