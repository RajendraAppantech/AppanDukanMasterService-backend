package com.appan.apimaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.apimaster.models.CreateKycApiMaster;
import com.appan.apimaster.repositories.ApiRepositories.KycApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.KycApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class KycApiMasterCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private KycApiMasterRepository kycApiMasterRepository;

	public CommonResponse createKycApi(@Valid CreateKycApiMaster req) {
		CommonResponse response = new CommonResponse();

		try {
			// Step 1: Check if the username exists
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the KYC API data already exists
			KycApiMaster existingKycApi = kycApiMasterRepository.findByApiNameAndCodeIgnoreCase(req.getApiName().trim(),
					req.getCode().trim());

			if (existingKycApi != null) {
				response.setStatus(false);
				response.setMessage("KYC API data already exists.");
				response.setRespCode("02");
				return response;
			}

			// Step 3: Insert new KYC API data
			KycApiMaster newKycApi = new KycApiMaster();
			newKycApi.setApiName(req.getApiName());
			newKycApi.setCode(req.getCode());
			newKycApi.setIsActive(req.getIsActive());
			newKycApi.setIsUserwise(req.getIsUserwise());
			newKycApi.setType(req.getType());
			newKycApi.setParam1(req.getParam1());
			newKycApi.setParam2(req.getParam2());
			newKycApi.setParam3(req.getParam3());
			newKycApi.setStatus(req.getStatus());
			newKycApi.setCreatedBy(req.getUsername().toUpperCase());
			newKycApi.setCreatedDt(new Date());
			newKycApi.setAuthStatus("4");

			kycApiMasterRepository.save(newKycApi);

			response.setStatus(true);
			response.setMessage("KYC API created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("An error occurred while processing the request.");
			response.setRespCode("EX");
		}

		return response;
	}
}
