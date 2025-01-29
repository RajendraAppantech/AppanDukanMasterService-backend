package com.appan.switchms.service;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SwitchOperatorMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.switchms.models.AuthOrBlockSwitchOperatorRequest;
import com.appan.switchms.repositories.SwitchMsRepositories.SwitchOperatorMasterRepository;
import com.google.gson.GsonBuilder;

@Service
public class AuthOrBlockSwitchOperatorService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(AuthOrBlockSwitchOperatorService.class);

	@Autowired
	private SwitchOperatorMasterRepository repository;

	public CommonResponse authorblock(AuthOrBlockSwitchOperatorRequest req) {
		Logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK SWITCH OPERATOR *************************************");
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			Optional<SwitchOperatorMaster> optionalMst = repository.findById(req.getId());
			if (!optionalMst.isPresent()) {
				response.setStatus(false);
				response.setMessage("Switch operator not found");
				response.setRespCode("01");
				return response;
			}

			SwitchOperatorMaster mst = optionalMst.get();

			if (req.getStatus().equalsIgnoreCase("1") && mst.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Switch operator already authorized.");
				response.setRespCode("01");
				return response;
			}

			mst.setAuthBy(req.getUsername().toUpperCase());
			mst.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				mst.setAuthStatus("1");
				response.setMessage("Switch operator authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				mst.setAuthStatus("3");
				response.setMessage("Switch operator blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			repository.save(mst);
			response.setStatus(true);
			response.setRespCode("00");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
