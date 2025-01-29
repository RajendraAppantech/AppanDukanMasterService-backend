package com.appan.user.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.user.model.ForgotPasswordRequest;

@Service
public class LogoutService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(LogoutService.class);

	public CommonResponse logout(ForgotPasswordRequest req) {
		Logger.info("\r\n\r\n**************************** LOGOUT *************************************");
		CommonResponse response = new CommonResponse();
		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage("Username " + ErrorMessages.NOT_FOUND);
				response.setRespCode("01");
				return response;
			}

			master.setLogoutStatus("Y");
			master.setLastLogoutDate(new Date());
			masterRepository.save(master);

			response.setStatus(true);
			response.setMessage(ErrorMessages.LOGOUT);
			response.setRespCode("00");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
			return response;
		}
	}
}