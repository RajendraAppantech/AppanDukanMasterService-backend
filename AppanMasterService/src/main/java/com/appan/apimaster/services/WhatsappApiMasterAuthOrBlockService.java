package com.appan.apimaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.WhatsappApiMasterAuthOrBlockRequest;
import com.appan.apimaster.repositories.ApiRepositories.WhatsappApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.WhatsappApiMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class WhatsappApiMasterAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private WhatsappApiMasterRepository whatsappApiMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(WhatsappApiMasterAuthOrBlockService.class);

	public CommonResponse authorBlockWhatsappApi(WhatsappApiMasterAuthOrBlockRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK WHATSAPP API *************************************");

		CommonResponse response = new CommonResponse();

		try {
			// Step 1: Check for user authentication
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the ID exists in the WhatsappApiMaster table
			WhatsappApiMaster apiRecord = whatsappApiMasterRepository.findById(req.getId()).orElse(null);
			if (apiRecord == null) {
				response.setStatus(false);
				response.setMessage("ID not found in Whatsapp API Master.");
				response.setRespCode("01");
				return response;
			}

			// Step 3: Check the current status and perform the required operation
			if (req.getStatus().equalsIgnoreCase("1") && apiRecord.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Whatsapp API is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && apiRecord.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Whatsapp API is already blocked.");
				response.setRespCode("01");
				return response;
			}

			// Perform the author/block operation based on the status
			apiRecord.setAuthBy(req.getUsername().toUpperCase());
			apiRecord.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				apiRecord.setAuthStatus("1");
				response.setMessage("Whatsapp API authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				apiRecord.setAuthStatus("3");
				response.setMessage("Whatsapp API blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			// Save the updated API record
			whatsappApiMasterRepository.save(apiRecord);

			// Set success response
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
