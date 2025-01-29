package com.appan.apimaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.apimaster.models.KycApiMasterAuthOrBlockRequest;
import com.appan.apimaster.repositories.ApiRepositories.KycApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.KycApiMaster; // Assuming KycApiMaster is the corresponding entity
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository; // Assuming this repository exists

@Service
public class KycApiMasterAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private KycApiMasterRepository kycApiMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(KycApiMasterAuthOrBlockService.class);

	public CommonResponse authorBlockKycApi(KycApiMasterAuthOrBlockRequest req) {
		logger.info("\r\n\r\n**************************** AUTH OR BLOCK KYC API *************************************");

		CommonResponse response = new CommonResponse();

		try {

			// Check if the username is valid
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid username.");
				response.setRespCode("01");
				return response;
			}

			// Check if the ID exists in the table
			KycApiMaster kycApi = kycApiMasterRepository.findById(req.getId()).orElse(null);
			if (kycApi == null) {
				response.setStatus(false);
				response.setMessage("KYC API ID not found.");
				response.setRespCode("01");
				return response;
			}

			// Check if the status is already in the required state
			if (req.getStatus().equalsIgnoreCase("1") && kycApi.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("KYC API is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && kycApi.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("KYC API is already blocked.");
				response.setRespCode("01");
				return response;
			}

			// Set the authorization details
			kycApi.setAuthBy(req.getUsername().toUpperCase());
			kycApi.setAuthDate(new Date());

			// Update the authorization status
			if (req.getStatus().equalsIgnoreCase("1")) {
				kycApi.setAuthStatus("1");
				response.setMessage("KYC API authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				kycApi.setAuthStatus("3");
				response.setMessage("KYC API blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			// Save the updated entity
			kycApiMasterRepository.save(kycApi);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}
}
