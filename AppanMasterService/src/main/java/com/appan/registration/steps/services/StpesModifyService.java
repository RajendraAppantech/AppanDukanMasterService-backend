package com.appan.registration.steps.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.RegistrationSteps;
import com.appan.entity.UserMaster;
import com.appan.registration.steps.model.StepsModifyRequest;
import com.appan.repositories.Repositories.StepsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class StpesModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private StepsMasterRepository repository;

	public CommonResponse modify(StepsModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<RegistrationSteps> mst = repository.findById(req.getId());
			if (!mst.isPresent()) {
				response.setStatus(false);
				response.setMessage("Registration steps with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			RegistrationSteps existingRegistrationsteps = repository.findByStepsNameAndLabel(req.getStepsName(), req.getLabel());
			if (existingRegistrationsteps != null && !existingRegistrationsteps.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Registration steps with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			RegistrationSteps ms = mst.get();
			ms.setStepsName(req.getStepsName());
			ms.setLabel(req.getLabel());
			ms.setUserType(req.getUserType());
			ms.setEntityType(req.getEntityType());
			ms.setRank(req.getRank());
			ms.setIsMandatory(req.getIsMandatory());
			ms.setIsSignup(req.getIsSignup());
			ms.setStatus(req.getStatus());
			ms.setModifyBy(req.getUsername().toUpperCase());
			ms.setModifyDt(new Date());
			repository.save(ms);

			response.setStatus(true);
			response.setMessage("Registration steps modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
