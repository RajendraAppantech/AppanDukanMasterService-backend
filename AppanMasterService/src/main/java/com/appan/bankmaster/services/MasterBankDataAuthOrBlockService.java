package com.appan.bankmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.entity.MasterBankData;
import com.appan.bankmaster.masterbankdata.model.MasterBankDataAuthOrBlockRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.MasterBankDataRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class MasterBankDataAuthOrBlockService {

	private static final Logger logger = LoggerFactory.getLogger(MasterBankDataAuthOrBlockService.class);

	@Autowired
	private MasterBankDataRepository masterBankDataRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse authorBlockMasterBankData(@Valid MasterBankDataAuthOrBlockRequest req) {
		logger.info(
				"**************************** AUTH OR BLOCK MASTER BANK DATA *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			MasterBankData masterBankData = masterBankDataRepository.findById(req.getId()).orElse(null);
			if (masterBankData == null) {
				response.setStatus(false);
				response.setMessage("Master Bank Data with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && masterBankData.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Master Bank Data already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && masterBankData.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Master Bank Data is already blocked.");
				response.setRespCode("01");
				return response;
			}

			masterBankData.setAuthBy(req.getUsername().toUpperCase());
			masterBankData.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				masterBankData.setAuthStatus("1");
				response.setMessage("Master Bank Data authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				masterBankData.setAuthStatus("3");
				response.setMessage("Master Bank Data blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			masterBankDataRepository.save(masterBankData);

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
