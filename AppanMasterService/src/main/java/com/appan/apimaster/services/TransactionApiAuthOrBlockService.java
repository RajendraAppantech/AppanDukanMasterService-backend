package com.appan.apimaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.TransactionApiAuthOrBlockRequest;
import com.appan.apimaster.repositories.ApiRepositories.TransactionApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TransactionApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class TransactionApiAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TransactionApiMasterRepository transactionApiMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(TransactionApiAuthOrBlockService.class);

	public CommonResponse authorBlockTransactionApi(TransactionApiAuthOrBlockRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK TRANSACTION API *************************************");

		CommonResponse response = new CommonResponse();

		try {

			// Step 1: Check if the user is valid
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the transaction ID exists
			TransactionApiMaster transactionApi = transactionApiMasterRepository.findById(req.getId()).orElse(null);
			if (transactionApi == null) {
				response.setStatus(false);
				response.setMessage("Transaction ID not found");
				response.setRespCode("01");
				return response;
			}

			// Step 3: Authorize or Block based on status
			if (req.getStatus().equalsIgnoreCase("1") && transactionApi.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Transaction API is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && transactionApi.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Transaction API is already blocked.");
				response.setRespCode("01");
				return response;
			}

			// Set authorization details
			transactionApi.setAuthBy(req.getUsername().toUpperCase());
			transactionApi.setAuthDate(new Date());

			// Update authorization status
			if (req.getStatus().equalsIgnoreCase("1")) {
				transactionApi.setAuthStatus("1");
				response.setMessage("Transaction API authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				transactionApi.setAuthStatus("3");
				response.setMessage("Transaction API blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			// Save the updated transaction API record
			transactionApiMasterRepository.save(transactionApi);

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
