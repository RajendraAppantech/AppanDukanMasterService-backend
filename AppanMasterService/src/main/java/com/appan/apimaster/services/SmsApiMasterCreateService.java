package com.appan.apimaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.CreateSmsApiMaster;
import com.appan.apimaster.repositories.ApiRepositories.SmsApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SmsApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class SmsApiMasterCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SmsApiMasterRepository smsApiMasterRepository;

	public CommonResponse createSmsApi(@Valid CreateSmsApiMaster req) {
		CommonResponse response = new CommonResponse();

		try {
			// Step 1: Authenticate user
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the record already exists
			SmsApiMaster existingRecord = smsApiMasterRepository
					.findByTenantNameAndApiUrlIgnoreCase(req.getTenantName().trim(), req.getApiUrl().trim());
			if (existingRecord != null) {
				response.setStatus(false);
				response.setMessage("SMS API data already exists for this tenant and API URL.");
				response.setRespCode("02");
				return response;
			}

			// Step 3: Create new record
			SmsApiMaster newSmsApi = new SmsApiMaster();
			newSmsApi.setTenantName(req.getTenantName());
			newSmsApi.setApiUrl(req.getApiUrl());
			newSmsApi.setBalApiUrl(req.getBalApiUrl());
			newSmsApi.setStatus(req.getStatus());
			newSmsApi.setCreatedBy(req.getUsername().toUpperCase());
			newSmsApi.setCreatedDt(new Date());
			
			newSmsApi.setAuthStatus("4");

			smsApiMasterRepository.save(newSmsApi);

			response.setStatus(true);
			response.setMessage("SMS API created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}

		return response;
	}
}
