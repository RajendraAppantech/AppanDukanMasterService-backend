package com.appan.profilemaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfilesMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.CreateProfilesMasterRequest;
import com.appan.repositories.Repositories.ProfilesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class CreateProfilesMasterService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ProfilesMasterRepository profilesMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(CreateProfilesMasterService.class);

	public CommonResponse create(@Valid CreateProfilesMasterRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			// Step 1: Authenticate username
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if data already exists in the table
			ProfilesMaster existingProfile = profilesMasterRepository
					.findByProfileNameAndCodeIgnoreCase(req.getProfileName().trim(), req.getCode().trim());

			if (existingProfile != null) {
				response.setStatus(false);
				response.setMessage("Profile with the same name and code already exists.");
				response.setRespCode("01");
				return response;
			}

			// Step 3: Add new data
			ProfilesMaster newProfile = new ProfilesMaster();
			newProfile.setProfileName(req.getProfileName().trim());
			newProfile.setCode(req.getCode().trim());
			newProfile.setUserType(req.getUserType().trim());
			newProfile.setSignupProfile(req.getSignupProfile());
			newProfile.setStatus(req.getStatus().trim());
			newProfile.setCreatedBy(req.getUsername().toUpperCase());
			newProfile.setCreatedDt(new Date());
			newProfile.setAuthStatus("4");
			newProfile.setMenu("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

			profilesMasterRepository.save(newProfile);

			response.setStatus(true);
			response.setMessage("Profile created successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating profile: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
