package com.appan.planmaster.services;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.planmaster.entity.RechargePlansMaster;
import com.appan.planmaster.rechargeplans.model.ModifyRechargePlanRequest;
import com.appan.planmaster.repositories.PlanMasterRepositories.RechargePlansMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyRechargePlanService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private RechargePlansMasterRepository rechargePlansMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyRechargePlanService.class);

	public CommonResponse modify(@Valid ModifyRechargePlanRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			Optional<RechargePlansMaster> planOptional = rechargePlansMasterRepository.findById(req.getId());

			if (!planOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("Recharge Plan with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			RechargePlansMaster existingPlan = rechargePlansMasterRepository.findByPlanIdAndCircleNameAndOperatorName(
					req.getPlanId(), req.getCircleName(), req.getOperatorName());

			if (existingPlan != null && !existingPlan.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("A plan with the given ID already exists for this circle and operator");
				response.setRespCode("02");
				return response;
			}

			RechargePlansMaster plan = planOptional.get();
			plan.setPlanId(req.getPlanId());
			plan.setPlanType(req.getPlanType());
			plan.setCircleName(req.getCircleName());
			plan.setOperatorName(req.getOperatorName());
			plan.setAmount(req.getAmount());
			plan.setValidity(req.getValidity());
			plan.setShortDesc(req.getShortDesc());
			plan.setLongDesc(req.getLongDesc());
			plan.setStatus(req.getStatus());
			plan.setModifyBy(req.getUsername().toUpperCase());
			plan.setModifyDt(new Date());

			rechargePlansMasterRepository.save(plan);

			response.setStatus(true);
			response.setMessage("Recharge Plan modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Exception in modifying Recharge Plan", e);

			response.setStatus(false);
			response.setMessage("Exception occurred while modifying the plan");
			response.setRespCode("03");
			return response;
		}
	}
}
