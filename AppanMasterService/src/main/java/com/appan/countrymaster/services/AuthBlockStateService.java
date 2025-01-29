package com.appan.countrymaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.state.models.AuthOrBlockStateRequest;
import com.appan.entity.StateMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.StateMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthBlockStateService {

	@Autowired
	private StateMasteRepository stateMasteRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(AuthBlockStateService.class);

	public CommonResponse authorblock(AuthOrBlockStateRequest req) {
		Logger.info("\r\n\r\n**************************** AUTH OR BLOCK STATE *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			StateMaster state = stateMasteRepository.findById(req.getId()).orElse(null);
			if (state == null) {
				response.setStatus(false);
				response.setMessage("State ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && state.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("State already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && state.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("State is already blocked.");
				response.setRespCode("01");
				return response;
			}

			state.setAuthBy(req.getUsername().toUpperCase());
			state.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				state.setAuthStatus("1");
				response.setMessage("State authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				state.setAuthStatus("3");
				response.setMessage("State blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			stateMasteRepository.save(state);

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
