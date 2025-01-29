package com.appan.apimaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.apimaster.models.EmailApiMasterAuthOrBlockRequest;
import com.appan.apimaster.repositories.ApiRepositories.EmailApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.EmailApiMaster; // Assuming you have a class for the EmailApiMaster entity
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class EmailApiMasterAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private EmailApiMasterRepository emailApiMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(EmailApiMasterAuthOrBlockService.class);

	public CommonResponse authorBlockEmailApi(EmailApiMasterAuthOrBlockRequest req) {
		logger.info("**************************** AUTH OR BLOCK EMAIL API *************************************");

		CommonResponse response = new CommonResponse();

		try {
			// Check if the user exists
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid username");
				response.setRespCode("01");
				return response;
			}

			// Check if the ID exists in the table
			EmailApiMaster emailApi = emailApiMasterRepository.findById(req.getId()).orElse(null);
			if (emailApi == null) {
				response.setStatus(false);
				response.setMessage("Email API ID not found");
				response.setRespCode("01");
				return response;
			}

			// Check if the action is already performed (Authorization or Block)
			if (req.getStatus().equalsIgnoreCase("1") && emailApi.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Email API is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && emailApi.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Email API is already blocked.");
				response.setRespCode("01");
				return response;
			}

			// Set the auth status and save the changes
			emailApi.setAuthBy(req.getUsername().toUpperCase());
			emailApi.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				emailApi.setAuthStatus("1"); // Authorized
				response.setMessage("Email API authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				emailApi.setAuthStatus("3"); // Blocked
				response.setMessage("Email API blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid status");
				return response;
			}

			// Save changes to the database
			emailApiMasterRepository.save(emailApi);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}
}
