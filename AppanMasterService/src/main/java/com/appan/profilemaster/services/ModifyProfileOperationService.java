package com.appan.profilemaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfileOperationMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.ModifyProfileOperationRequest;
import com.appan.repositories.Repositories.ProfileOperationMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyProfileOperationService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ProfileOperationMasterRepository profileMasterRepository;

	public CommonResponse modify(@Valid ModifyProfileOperationRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			// Check if user exists for authentication
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Check if the entry exists by ID
			Optional<ProfileOperationMaster> existingProfileOpt = profileMasterRepository.findById(req.getId());
			if (!existingProfileOpt.isPresent()) {
				response.setStatus(false);
				response.setMessage("Profile with ID " + req.getId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			ProfileOperationMaster existingProfile = existingProfileOpt.get();

			// Check if the profile with the same profile name already exists, excluding the
			// current profile
			boolean isDuplicate = profileMasterRepository.existsByProfileNameIgnoreCaseAndIdNot(req.getProfileName(),
					req.getId());
			if (isDuplicate) {
				response.setStatus(false);
				response.setMessage("Duplicate profile name: The profile name already exists.");
				response.setRespCode("02");
				return response;
			}

			// Modify the profile operation
			existingProfile.setOperationName(req.getOperationName());
			existingProfile.setProfileName(req.getProfileName());
			existingProfile.setStartDate(req.getStartDate());
			existingProfile.setEndDate(req.getEndDate());
			existingProfile.setIsDateValidity(req.getIsDateValidity());
			existingProfile.setIsOnlyUi(req.getIsOnlyUi());
			existingProfile.setStatus(req.getStatus());
			existingProfile.setModifyBy(req.getUsername().toUpperCase());
			existingProfile.setModifyDt(new Date());

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
