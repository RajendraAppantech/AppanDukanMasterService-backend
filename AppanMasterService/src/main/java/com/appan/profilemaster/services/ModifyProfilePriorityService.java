package com.appan.profilemaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfilePriorityMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.ModifyProfilePriorityRequest;
import com.appan.repositories.Repositories.ProfilePriorityMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyProfilePriorityService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ProfilePriorityMasterRepository profileMasterRepository;

	public CommonResponse modify(@Valid ModifyProfilePriorityRequest req) {

		CommonResponse response = new CommonResponse();

		try {
			// Check if the user exists for authentication
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Check if the profile entry exists by ID
			Optional<ProfilePriorityMaster> existingProfileOpt = profileMasterRepository.findById(req.getId());
			if (!existingProfileOpt.isPresent()) {
				response.setStatus(false);
				response.setMessage("Profile with ID " + req.getId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			ProfilePriorityMaster existingProfile = existingProfileOpt.get();



			// Proceed with the update
			existingProfile.setUserName(req.getUserName());
			existingProfile.setAllowedProfile1(req.getAllowedProfile1());
			existingProfile.setAllowedProfile2(req.getAllowedProfile2());
			existingProfile.setAllowedProfile3(req.getAllowedProfile3());
			existingProfile.setAllowedProfile4(req.getAllowedProfile4());
			existingProfile.setAllowedProfile5(req.getAllowedProfile5());
			existingProfile.setStatus(req.getStatus());
			existingProfile.setModifyBy(req.getUsername().toUpperCase());
			existingProfile.setModifyDt(new Date());

			profileMasterRepository.save(existingProfile);

			response.setStatus(true);
			response.setMessage("Profile updated successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
