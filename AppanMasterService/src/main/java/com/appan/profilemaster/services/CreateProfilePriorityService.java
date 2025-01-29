package com.appan.profilemaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfilePriorityMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.CreateProfilePriorityRequest;
import com.appan.repositories.Repositories.ProfilePriorityMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class CreateProfilePriorityService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ProfilePriorityMasterRepository profilePriorityRepository;

	private static final Logger logger = LoggerFactory.getLogger(CreateProfilePriorityService.class);

	public CommonResponse create(@Valid CreateProfilePriorityRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			// Step 1: Authenticate the username
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the data already exists
			ProfilePriorityMaster existingProfile = profilePriorityRepository
					.findByUserTypeAndUserName(req.getUserType().trim(), req.getUserName().trim());
			if (existingProfile != null) {
				response.setStatus(false);
				response.setMessage("Profile priority with the same userType and userName already exists.");
				response.setRespCode("01");
				return response;
			}

			// Step 3: Add the new profile priority data
			ProfilePriorityMaster newProfile = new ProfilePriorityMaster();
			newProfile.setUserType(req.getUserType().trim());
			newProfile.setUserName(req.getUserName().trim());
			newProfile.setAllowedProfile1(req.getAllowedProfile1());
			newProfile.setAllowedProfile2(req.getAllowedProfile2());
			newProfile.setAllowedProfile3(req.getAllowedProfile3());
			newProfile.setAllowedProfile4(req.getAllowedProfile4());
			newProfile.setAllowedProfile5(req.getAllowedProfile5());
			newProfile.setStatus(req.getStatus());
			newProfile.setCreatedBy(req.getUsername().toUpperCase());
			newProfile.setCreatedDt(new Date());
			newProfile.setAuthStatus("4");

			profilePriorityRepository.save(newProfile);

			// Step 4: Return success response
			response.setStatus(true);
			response.setMessage("Profile priority created successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating profile priority: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
