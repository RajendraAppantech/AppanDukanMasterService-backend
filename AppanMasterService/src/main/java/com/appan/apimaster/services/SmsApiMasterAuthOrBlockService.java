package com.appan.apimaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.SmsApiMasterAuthOrBlockRequest;
import com.appan.apimaster.repositories.ApiRepositories.SmsApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SmsApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class SmsApiMasterAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SmsApiMasterRepository smsApiMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(SmsApiMasterAuthOrBlockService.class);

	public CommonResponse authorBlockSmsApi(SmsApiMasterAuthOrBlockRequest req) {
		logger.info("\r\n\r\n**************************** AUTH OR BLOCK SMS API *************************************");

		CommonResponse response = new CommonResponse();

		try {
			// Check if the user exists in the UserMaster table
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Check if the SMS record with the provided ID exists in the SmsApiMaster table
			SmsApiMaster smsApi = smsApiMasterRepository.findById(req.getId()).orElse(null);
			if (smsApi == null) {
				response.setStatus(false);
				response.setMessage("SMS API ID not found");
				response.setRespCode("01");
				return response;
			}

			// Check if the status is already the same
			if (req.getStatus().equalsIgnoreCase("1") && smsApi.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("SMS API is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && smsApi.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("SMS API is already blocked.");
				response.setRespCode("01");
				return response;
			}

			// Set the authorization details
			smsApi.setAuthBy(req.getUsername().toUpperCase());
			smsApi.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				smsApi.setAuthStatus("1"); // Authorized
				response.setMessage("SMS API authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				smsApi.setAuthStatus("3"); // Blocked
				response.setMessage("SMS API blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			// Save the updated status in the repository
			smsApiMasterRepository.save(smsApi);

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
