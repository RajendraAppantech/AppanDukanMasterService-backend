package com.appan.apimaster.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.ModifyWhatsappApiMaster;
import com.appan.apimaster.repositories.ApiRepositories.WhatsappApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.WhatsappApiMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class WhatsappApiMasterModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private WhatsappApiMasterRepository whatsappApiMasterRepository;

	public CommonResponse modifyWhatsappApi(ModifyWhatsappApiMaster req) {
		CommonResponse response = new CommonResponse();

		try {

			// Step 1: Check username for authentication
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the entry exists in the table by ID
			Optional<WhatsappApiMaster> apiOptional = whatsappApiMasterRepository.findById(req.getId());
			if (!apiOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("API with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			// Step 3: Check if the updated data already exists in the table
			WhatsappApiMaster existingApi = whatsappApiMasterRepository.findByTenantNameAndApiUrl(req.getTenantName(),
					req.getApiUrl());

			if (existingApi != null && !existingApi.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("API with the same tenant name and API URL already exists");
				response.setRespCode("02");
				return response;
			}

			// Step 4: Modify the existing data
			WhatsappApiMaster apiMaster = apiOptional.get();
			apiMaster.setTenantName(req.getTenantName());
			apiMaster.setApiUrl(req.getApiUrl());
			apiMaster.setBalApiUrl(req.getBalApiUrl());
			apiMaster.setStatus(req.getStatus());
			apiMaster.setModifyBy(req.getUsername().toUpperCase());

			whatsappApiMasterRepository.save(apiMaster);

			// Return success response
			response.setStatus(true);
			response.setMessage("API modified successfully.");
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
