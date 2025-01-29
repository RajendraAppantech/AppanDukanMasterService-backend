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
import com.appan.user.model.UserRequest;
import com.google.gson.GsonBuilder;

@Service
public class ModifyUserService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(ModifyUserService.class);

	public CommonResponse modifyUser(UserRequest req) {
		Logger.info("\r\n\r\n**************************** MODIFY USER *************************************");
		CommonResponse response = new CommonResponse();
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserMaster ms = masterRepository.findByUserId(req.getUserId().toUpperCase());
			if (ms == null) {
				response.setStatus(false);
				response.setMessage("User " + ErrorMessages.NOT_FOUND);
				response.setRespCode("01");
				return response;
			}

			UserMaster master1 = masterRepository.findByMobileNo(req.getMobileNo());
			if (master1 != null) {
				if (!ms.getUserId().equalsIgnoreCase(master1.getUserId())) {
					response.setStatus(false);
					response.setMessage(ErrorMessages.EXIST_MOBILENO);
					response.setRespCode("02");
					return response;
				}
			}

			ms.setName(req.getName().toUpperCase());
			ms.setMobileNo(req.getMobileNo());
			ms.setEmailId(req.getEmailId());
			// ms.setUserRole(req.getUserRole().toUpperCase());
			// ms.setUserProfile(req.getUserProfile().toUpperCase());
			// ms.setStatus("1");
			// ms.setCustomerCode(req.getCustomerCode());
			ms.setModifyBy(req.getUsername().toUpperCase());
			ms.setModifyDt(new Date());
			masterRepository.save(ms);

			response.setStatus(true);
			response.setMessage("User " + ErrorMessages.MODIFY_SUCCESS);
			response.setRespCode("00");
			return response;
		} catch (

		Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
			return response;
		}
	}
}