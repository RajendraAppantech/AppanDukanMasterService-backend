package com.appan.registration.steps.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.RegistrationSteps;
import com.appan.entity.UserMaster;
import com.appan.registration.steps.model.StepsCreateRequest;
import com.appan.repositories.Repositories.StepsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class StepsCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private StepsMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(StepsCreateService.class);

	public CommonResponse create(@Valid StepsCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			RegistrationSteps existingWallet = repository.findByStepsNameAndLabelIgnoreCase(req.getStepsName().trim(), req.getLabel().trim());
			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Registation Steps with the same name and lable already exists.");
				response.setRespCode("01");
				return response;
			}

			RegistrationSteps newWallet = new RegistrationSteps();
			newWallet.setStepsName(req.getStepsName());
			newWallet.setLabel(req.getLabel());
			newWallet.setUserType(req.getUserType());
			newWallet.setEntityType(req.getEntityType());
			newWallet.setRank(req.getRank());
			newWallet.setIsMandatory(req.getIsMandatory());
			newWallet.setIsSignup(req.getIsSignup());
			newWallet.setCreatedBy(req.getUsername().toUpperCase());
			newWallet.setStatus(req.getStatus());
			newWallet.setCreatedDt(new Date());
			newWallet.setAuthStatus("4");
			repository.save(newWallet);

			response.setStatus(true);
			response.setMessage("Registation Steps created successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating wallet: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
