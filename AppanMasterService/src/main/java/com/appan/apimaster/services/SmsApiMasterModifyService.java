package com.appan.apimaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.apimaster.models.ModifySmsApiMaster;
import com.appan.apimaster.repositories.ApiRepositories.SmsApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SmsApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class SmsApiMasterModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SmsApiMasterRepository smsApiMasterRepository;

	public CommonResponse modifySmsApi(@Valid ModifySmsApiMaster req) {
		CommonResponse response = new CommonResponse();

		try {
			// Step 1: Validate the user
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid username.");
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the record with the given ID exists
			Optional<SmsApiMaster> smsApiMasterOptional = smsApiMasterRepository.findById(req.getId());
			if (!smsApiMasterOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("API record with the given ID not found.");
				response.setRespCode("01");
				return response;
			}

			SmsApiMaster existingSmsApiMaster = smsApiMasterRepository.findByTenantNameAndApiUrl(req.getTenantName(),
					req.getApiUrl());

			// Step 3: Check if updating data already exists in the table
			if (existingSmsApiMaster != null && !existingSmsApiMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("API record with the same tenant name and API URL already exists.");
				response.setRespCode("02");
				return response;
			}

			// Step 4: Modify the data
			SmsApiMaster smsApiMaster = smsApiMasterOptional.get();
			smsApiMaster.setTenantName(req.getTenantName());
			smsApiMaster.setBalApiUrl(req.getBalApiUrl());
			smsApiMaster.setApiUrl(req.getApiUrl());
			smsApiMaster.setStatus(req.getStatus());
			smsApiMaster.setModifyBy(req.getUsername().toUpperCase());
			smsApiMaster.setModifyDt(new Date());

			smsApiMasterRepository.save(smsApiMaster);

			response.setStatus(true);
			response.setMessage("API record modified successfully.");
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
