package com.appan.usermenu.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.usermenu.model.FetchUserMenuReq;

@Service
public class FetchUserMenuService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchUserMenuService.class);

	public CommonResponse fetchUserMenu(FetchUserMenuReq req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserMaster userByUserId = masterRepository.findByUserId(req.getUserId().toUpperCase());
			if (userByUserId == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.NOT_FOUND);
				response.setRespCode("02");
				return response;
			}

			if (!userByUserId.getUserProfile().equalsIgnoreCase(req.getUserProfile())) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.NOT_FOUND);
				response.setRespCode("03");
				return response;
			}

			response.setStatus(true);
			response.setMessage("User Menu found successfully");
			response.setRespCode("00");
			response.setData(Map.of("userMenu", userByUserId.getUserMenu()));

			return response;

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
			return response;
		}
	}
}