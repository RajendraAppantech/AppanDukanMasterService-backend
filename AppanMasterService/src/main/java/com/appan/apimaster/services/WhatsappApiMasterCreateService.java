package com.appan.apimaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.apimaster.models.CreateWhatsappApiMaster;
import com.appan.apimaster.repositories.ApiRepositories.WhatsappApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.WhatsappApiMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class WhatsappApiMasterCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private WhatsappApiMasterRepository whatsappApiMasterRepository;

	public CommonResponse createWhatsappApi(@Valid CreateWhatsappApiMaster req) {
		CommonResponse response = new CommonResponse();

		try {
			// Step 1: Authenticate the username
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid username.");
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the API data already exists in the table
			WhatsappApiMaster existingApi = whatsappApiMasterRepository.findByTenantNameAndApiUrl(req.getTenantName(),
					req.getApiUrl());

			if (existingApi != null) {
				response.setStatus(false);
				response.setMessage("API with this tenant name and API URL already exists.");
				response.setRespCode("01");
				return response;
			}

			// Step 3: Create the new API entry
			WhatsappApiMaster newApi = new WhatsappApiMaster();
			newApi.setTenantName(req.getTenantName());
			newApi.setApiUrl(req.getApiUrl());
			newApi.setBalApiUrl(req.getBalApiUrl());
			newApi.setStatus(req.getStatus());

			newApi.setAuthStatus("4");

			// Save the new API data to the repository
			whatsappApiMasterRepository.save(newApi);

			// Return success response
			response.setStatus(true);
			response.setMessage("Whatsapp API created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("An exception occurred while creating the API.");
			response.setRespCode("EX");
		}

		return response;
	}
}
