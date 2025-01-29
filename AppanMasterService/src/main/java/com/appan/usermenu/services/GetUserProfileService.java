package com.appan.usermenu.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.google.gson.GsonBuilder;

@Service
public class GetUserProfileService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	@Autowired
	private UserMenuRepository menuRepository;

	private static final Logger logger = LoggerFactory.getLogger(GetUserProfileService.class);

	public CommonResponse getAllUserProfile() {
		CommonResponse response = new CommonResponse();

		try {
			List<String> userProfiles = menuRepository.findAllUserProfiles();

			response.setStatus(true);
			response.setMessage("User profiles fetched successfully");
			response.setRespCode("00");
			response.setData("userProfiles", userProfiles);
		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
		}
		return response;
	}
}
