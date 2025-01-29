package com.appan.bankmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.entity.MasterBankData;
import com.appan.bankmaster.masterbankdata.model.MasterBankDataCreateRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.MasterBankDataRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class MasterBankDataCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private MasterBankDataRepository masterBankDataRepository;

	private static final Logger logger = LoggerFactory.getLogger(MasterBankDataCreateService.class);

	public CommonResponse createMasterBankData(@Valid MasterBankDataCreateRequest req) {
		logger.info("**************************** CREATE MasterBankData *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			boolean isDuplicate = masterBankDataRepository.existsByBankNameAndIfscCodeAndBranch(req.getBankName(),
					req.getIfscCode(), req.getBranch());
			if (isDuplicate) {
				logger.error("Duplicate data found for bank '{}', IFSC code '{}', and branch '{}'", req.getBankName(),
						req.getIfscCode(), req.getBranch());
				response.setStatus(false);
				response.setMessage("Duplicate data: Master bank account already exists.");
				response.setRespCode("01");
				return response;
			}

			MasterBankData masterBankData = new MasterBankData();
			masterBankData.setBankName(req.getBankName());
			masterBankData.setIfscCode(req.getIfscCode());
			masterBankData.setBranch(req.getBranch());
			masterBankData.setAddress(req.getAddress());
			masterBankData.setContact(req.getContact());
			masterBankData.setCity(req.getCity());
			masterBankData.setState(req.getState());
			masterBankData.setBlock(req.getBlock());
			masterBankData.setCreatedBy(req.getUsername().toUpperCase());
			masterBankData.setCreatedDt(new Date());
			masterBankData.setAuthStatus("4");

			masterBankDataRepository.save(masterBankData);

			response.setStatus(true);
			response.setMessage("Master Bank Data created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while creating the Master Bank Data.");
			response.setRespCode("EX");
		}

		return response;
	}
}
