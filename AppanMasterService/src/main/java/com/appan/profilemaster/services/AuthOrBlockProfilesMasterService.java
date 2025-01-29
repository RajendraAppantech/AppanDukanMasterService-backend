package com.appan.profilemaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfilesMaster; 
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.AuthOrBlockProfilesMasterRequest;
import com.appan.repositories.Repositories.ProfilesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthOrBlockProfilesMasterService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ProfilesMasterRepository profilesMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(AuthOrBlockProfilesMasterService.class);

	public CommonResponse authorblock(AuthOrBlockProfilesMasterRequest req) {
		Logger.info("**************************** AUTH OR BLOCK PROFILE *************************************");

		CommonResponse response = new CommonResponse();

		try {

			// Step 1: Check if the username exists for authentication
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			// Step 2: Check if the profile ID exists
			ProfilesMaster profile = profilesMasterRepository.findById(req.getId()).orElse(null);
			if (profile == null) {
				response.setStatus(false);
				response.setMessage("Profile ID not found");
				response.setRespCode("01");
				return response;
			}

			// Step 3: Check the current authStatus and take action based on the requested
			// status
			if (req.getStatus().equalsIgnoreCase("1") && profile.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Profile already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && profile.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Profile is already blocked.");
				response.setRespCode("01");
				return response;
			}

			// Update the profile's auth status and set additional details
			profile.setAuthBy(req.getUsername().toUpperCase());
			profile.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				profile.setAuthStatus("1"); // Authorized
				response.setMessage("Profile authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				profile.setAuthStatus("3"); // Blocked
				response.setMessage("Profile blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			profilesMasterRepository.save(profile); // Save updated profile details

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
