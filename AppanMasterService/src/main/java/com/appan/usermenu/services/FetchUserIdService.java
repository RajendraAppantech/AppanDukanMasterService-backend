package com.appan.usermenu.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.usermenu.model.FetchUserIdRequest;
import com.google.gson.GsonBuilder;

import jakarta.validation.Valid;

@Service
public class FetchUserIdService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchUserIdService.class);

	public CommonResponse fetchuserid(@Valid FetchUserIdRequest req) {

		logger.info("\r\n\r\n**************************** FETCH USER ID *************************************");

		CommonResponse response = new CommonResponse();

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			List<String> userIds = masterRepository.findUserIdsByRoleAndProfile(req.getUserRole().toUpperCase(),
					req.getUserProfile().toUpperCase());
			if (userIds.isEmpty()) {
				response.setStatus(false);
				response.setMessage("User id " + ErrorMessages.NOT_FOUND);
				response.setRespCode("02");
				return response;
			}

			response.setStatus(true);
			response.setMessage("User IDs fetched successfully");
			response.setRespCode("00");
			response.setData("userIds", userIds);

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
		}

		return response;
	}
}