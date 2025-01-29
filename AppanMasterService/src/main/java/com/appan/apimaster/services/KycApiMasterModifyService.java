package com.appan.apimaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.ModifyKycApiMaster;
import com.appan.apimaster.repositories.ApiRepositories.KycApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.KycApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class KycApiMasterModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private KycApiMasterRepository kycApiMasterRepository;

	public CommonResponse modifyKycApi(ModifyKycApiMaster req) {
		CommonResponse response = new CommonResponse();

		try {
			// Step 1: Check username authentication
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the entry exists in the table by id
			Optional<KycApiMaster> kycApiOptional = kycApiMasterRepository.findById(req.getId());
			if (!kycApiOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("KYC API with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			KycApiMaster existingKycApi = kycApiMasterRepository.findByApiNameAndCode(req.getApiName(), req.getCode());

			// Step 3: Check if the same data already exists in the table
			if (existingKycApi != null && !existingKycApi.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("KYC API with the given API Name and Code already exists");
				response.setRespCode("02");
				return response;
			}

			// Step 4: Modify the data
			KycApiMaster kycApi = kycApiOptional.get();
			kycApi.setApiName(req.getApiName());
			kycApi.setCode(req.getCode());
			kycApi.setIsActive(req.getIsActive());
			kycApi.setIsUserwise(req.getIsUserwise());
			kycApi.setType(req.getType());
			kycApi.setParam1(req.getParam1());
			kycApi.setParam2(req.getParam2());
			kycApi.setParam3(req.getParam3());
			kycApi.setStatus(req.getStatus());
			kycApi.setModifyBy(req.getUsername().toUpperCase());
			kycApi.setModifyDt(new Date());

			// Save the modified data
			kycApiMasterRepository.save(kycApi);

			response.setStatus(true);
			response.setMessage("KYC API modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();

			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
