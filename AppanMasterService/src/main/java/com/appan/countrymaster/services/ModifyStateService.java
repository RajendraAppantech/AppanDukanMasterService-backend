package com.appan.countrymaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.state.models.ModifyStateRequest;
import com.appan.entity.StateMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.StateMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyStateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private StateMasteRepository stateMasteRepository;

	public CommonResponse modify(@Valid ModifyStateRequest req) {

		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<StateMaster> existingState = stateMasteRepository.findById(req.getId());
			if (!existingState.isPresent()) {
				response.setStatus(false);
				response.setMessage("State with ID " + req.getId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			existingState.get().setStateName(req.getStateName().toUpperCase());
			existingState.get().setCountryName(req.getCountryName().toUpperCase());
			existingState.get().setFingpayStateId(req.getFingpayStateId());
			existingState.get().setGiftCardStateId(req.getGiftCardStateId());
			existingState.get().setStatus(req.getStatus());
			existingState.get().setModifyBy(req.getUsername().toUpperCase());
			existingState.get().setModifyDt(new Date());

			stateMasteRepository.save(existingState.get());

			response.setStatus(true);
			response.setMessage("State updated successfully.");
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
