package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.state.models.CreateStateRequest;
import com.appan.entity.StateMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.StateMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class CreateStateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private StateMasteRepository stateMasteRepository;

	public CommonResponse create(CreateStateRequest req) {
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			StateMaster existingState = stateMasteRepository.findByStateName(req.getStateName().toUpperCase().trim());
			if (existingState != null) {
				response.setStatus(false);
				response.setMessage("State name already exists");
				response.setRespCode("01");
				return response;
			}

			StateMaster newState = new StateMaster();
			newState.setStateName(req.getStateName().toUpperCase());
			newState.setCountryName(req.getCountryName().toUpperCase());
			newState.setFingpayStateId(req.getFingpayStateId());
			newState.setGiftCardStateId(req.getGiftCardStateId());
			newState.setStatus(req.getStatus());
			newState.setCreatedBy(req.getUsername().toUpperCase());
			newState.setCreatedDt(new Date());
			newState.setAuthStatus("4");

			stateMasteRepository.save(newState);

			response.setStatus(true);
			response.setMessage("State created successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
