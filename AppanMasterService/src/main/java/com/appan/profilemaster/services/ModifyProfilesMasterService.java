package com.appan.profilemaster.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfilesMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.ModifyProfilesMasterRequest;
import com.appan.repositories.Repositories.ProfilesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyProfilesMasterService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ProfilesMasterRepository profileMasterRepository; 

	public CommonResponse modify(@Valid ModifyProfilesMasterRequest req) {

		CommonResponse response = new CommonResponse();

		try {
			// Check for authentication based on the username
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Check if the entry exists by ID
			Optional<ProfilesMaster> existingProfileOpt = profileMasterRepository.findById(req.getId());
			if (!existingProfileOpt.isPresent()) {
				response.setStatus(false);
				response.setMessage("Profile with ID " + req.getId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			// Check if the data to be updated already exists (for uniqueness)
			boolean isDuplicate = profileMasterRepository.existsByProfileNameIgnoreCaseAndCodeIgnoreCaseAndIdNot(
					req.getProfileName(), req.getCode(), req.getId());
			if (isDuplicate) {
				response.setStatus(false);
				response.setMessage("Duplicate entry: The combination of Profile Name and Code already exists.");
				response.setRespCode("02");
				return response;
			}

			// Modify the profile data
			ProfilesMaster existingProfile = existingProfileOpt.get();
			existingProfile.setProfileName(req.getProfileName());
			existingProfile.setCode(req.getCode());
			existingProfile.setUserType(req.getUserType());
			existingProfile.setSignupProfile(req.getSignupProfile());
			existingProfile.setStatus(req.getStatus());
			existingProfile.setModifyBy(req.getUsername().toUpperCase());
			existingProfile.setModifyDt(new java.util.Date());

			// Save the updated profile
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
