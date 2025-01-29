package com.appan.profilemaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfileOperationMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.AuthOrBlockProfileOperationRequest;
import com.appan.repositories.Repositories.ProfileOperationMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthOrBlockProfileOperationService {

	private static final Logger Logger = LoggerFactory.getLogger(AuthOrBlockProfileOperationService.class);

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ProfileOperationMasterRepository profileMasterRepository;

	public CommonResponse authorblock(AuthOrBlockProfileOperationRequest req) {

		CommonResponse response = new CommonResponse();

		try {
			// Check if username exists
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Check if profile exists by ID
			ProfileOperationMaster profile = profileMasterRepository.findById(req.getId()).orElse(null);
			if (profile == null) {
				response.setStatus(false);
				response.setMessage("Profile with ID " + req.getId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			// Check if profile status is already as requested
			if (req.getStatus().equalsIgnoreCase("1") && profile.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Profile is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && profile.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Profile is already blocked.");
				response.setRespCode("01");
				return response;
			}

			// Update the profile status based on the request
			profile.setAuthBy(req.getUsername().toUpperCase());
			profile.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				profile.setAuthStatus("1");
				response.setMessage("Profile authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				profile.setAuthStatus("3");
				response.setMessage("Profile blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			profileMasterRepository.save(profile);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
