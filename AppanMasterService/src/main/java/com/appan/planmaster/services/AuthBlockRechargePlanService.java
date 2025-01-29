package com.appan.planmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.planmaster.entity.RechargePlansMaster;
import com.appan.planmaster.rechargeplans.model.AuthOrBlockRechargePlanRequest;
import com.appan.planmaster.repositories.PlanMasterRepositories.RechargePlansMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthBlockRechargePlanService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private RechargePlansMasterRepository rechargePlansMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockRechargePlanService.class);

	public CommonResponse authorblock(AuthOrBlockRechargePlanRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK RECHARGE PLAN *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			RechargePlansMaster rechargePlan = rechargePlansMasterRepository.findById(req.getId()).orElse(null);
			if (rechargePlan == null) {
				response.setStatus(false);
				response.setMessage("Recharge Plan ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && rechargePlan.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Recharge Plan is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && rechargePlan.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Recharge Plan is already blocked.");
				response.setRespCode("01");
				return response;
			}

			rechargePlan.setAuthBy(req.getUsername().toUpperCase());
			rechargePlan.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				rechargePlan.setAuthStatus("1");
				response.setMessage("Recharge Plan authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				rechargePlan.setAuthStatus("3");
				response.setMessage("Recharge Plan blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			rechargePlansMasterRepository.save(rechargePlan);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
