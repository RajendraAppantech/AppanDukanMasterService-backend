package com.appan.planmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.planmaster.circle.model.CreateCircleRequest;
import com.appan.planmaster.entity.CirclesMaster;
import com.appan.planmaster.repositories.PlanMasterRepositories.CirclesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class CreateCircleService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CirclesMasterRepository circlesMasterRepository;
	private static final Logger logger = LoggerFactory.getLogger(CreateCircleService.class);

	public CommonResponse create(@Valid CreateCircleRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			CirclesMaster existingCircle = circlesMasterRepository.findByCircleCode1OrCircleCode2OrCircleCode3(
					req.getCircleCode1().trim(), req.getCircleCode2().trim(), req.getCircleCode3().trim());

			if (existingCircle != null) {
				response.setStatus(false);
				response.setMessage("Circle with the same codes already exists.");
				response.setRespCode("01");
				return response;
			}

			CirclesMaster newCircle = new CirclesMaster();
			newCircle.setCircleName(req.getCircleName());
			newCircle.setCode(req.getCode());
			newCircle.setCircleCode1(req.getCircleCode1());
			newCircle.setCircleCode2(req.getCircleCode2());
			newCircle.setCircleCode3(req.getCircleCode3());
			newCircle.setStatus(req.getStatus());
			newCircle.setCreatedBy(req.getUsername().toUpperCase());
			newCircle.setCreatedDt(new Date());
			newCircle.setAuthStatus("4");

			circlesMasterRepository.save(newCircle);

			response.setStatus(true);
			response.setMessage("Circle created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("Exception while creating circle: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred while creating circle.");
			response.setRespCode("EX");
		}

		return response;
	}
}
