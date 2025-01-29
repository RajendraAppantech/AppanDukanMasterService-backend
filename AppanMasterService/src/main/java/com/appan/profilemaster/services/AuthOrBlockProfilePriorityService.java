package com.appan.profilemaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfilePriorityMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.AuthOrBlockProfilePriorityRequest;
import com.appan.repositories.Repositories.ProfilePriorityMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class AuthOrBlockProfilePriorityService {

	@Autowired
	private ProfilePriorityMasterRepository profileMasterRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(AuthOrBlockProfilePriorityService.class);

	public CommonResponse authorblock(@Valid AuthOrBlockProfilePriorityRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			// Check if username exists for authentication
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			// Check if Profile ID exists
			ProfilePriorityMaster profile = profileMasterRepository.findById(req.getId()).orElse(null);
			if (profile == null) {
				response.setStatus(false);
				response.setMessage("Profile ID not found");
				response.setRespCode("01");
				return response;
			}

			// Check the current auth status and take action based on the provided status
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

			// Set authentication details
			profile.setAuthBy(req.getUsername().toUpperCase());
			profile.setAuthDate(new Date());

			// Update auth status based on the request status
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

			// Save the updated profile data
			profileMasterRepository.save(profile);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}
}
