package com.appan.planmaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.planmaster.entity.RechargePlansMaster;
import com.appan.planmaster.rechargeplans.model.CreateRechargePlanRequest;
import com.appan.planmaster.repositories.PlanMasterRepositories.RechargePlansMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class CreateRechargePlanService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private RechargePlansMasterRepository rechargePlansMasterRepository;

	public CommonResponse create(@Valid CreateRechargePlanRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			RechargePlansMaster existingPlan = rechargePlansMasterRepository.findByPlanIdAndCircleNameAndOperatorName(
					req.getPlanId().trim(), req.getCircleName().trim(), req.getOperatorName().trim());

			if (existingPlan != null) {
				response.setStatus(false);
				response.setMessage("Recharge Plan already exists for this Circle and Operator.");
				response.setRespCode("01");
				return response;
			}

			RechargePlansMaster newRechargePlan = new RechargePlansMaster();
			newRechargePlan.setPlanId(req.getPlanId());
			newRechargePlan.setPlanType(req.getPlanType());
			newRechargePlan.setCircleName(req.getCircleName());
			newRechargePlan.setOperatorName(req.getOperatorName());
			newRechargePlan.setAmount(req.getAmount());
			newRechargePlan.setValidity(req.getValidity());
			newRechargePlan.setShortDesc(req.getShortDesc());
			newRechargePlan.setLongDesc(req.getLongDesc());
			newRechargePlan.setStatus(req.getStatus());
			newRechargePlan.setAuthStatus("4");
			newRechargePlan.setCreatedBy(req.getUsername().toUpperCase());
			newRechargePlan.setCreatedDt(new Date());

			rechargePlansMasterRepository.save(newRechargePlan);

			response.setStatus(true);
			response.setMessage("Recharge Plan created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("Exception occurred while creating Recharge Plan.");
			response.setRespCode("EX");
			return response;
		}

		return response;
	}
}
