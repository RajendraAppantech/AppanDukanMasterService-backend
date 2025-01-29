package com.appan.planmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.planmaster.circle.model.AuthOrBlockCircleRequest;
import com.appan.planmaster.entity.CirclesMaster;
import com.appan.planmaster.repositories.PlanMasterRepositories.CirclesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthBlockCircleService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CirclesMasterRepository circlesMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockCircleService.class);

	public CommonResponse authorblock(AuthOrBlockCircleRequest req) {
		logger.info("\r\n\r\n**************************** AUTH OR BLOCK CIRCLE *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			CirclesMaster circle = circlesMasterRepository.findById(req.getId()).orElse(null);

			if (circle == null) {
				response.setStatus(false);
				response.setMessage("Circle with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && circle.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Circle is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && circle.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Circle is already blocked.");
				response.setRespCode("01");
				return response;
			}

			circle.setAuthBy(req.getUsername().toUpperCase());
			circle.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				circle.setAuthStatus("1");
				response.setMessage("Circle authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				circle.setAuthStatus("3");
				response.setMessage("Circle blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			circlesMasterRepository.save(circle);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION: " + e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}
}
