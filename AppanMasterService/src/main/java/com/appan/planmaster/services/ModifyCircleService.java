package com.appan.planmaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.planmaster.circle.model.ModifyCircleRequest;
import com.appan.planmaster.entity.CirclesMaster;
import com.appan.planmaster.repositories.PlanMasterRepositories.CirclesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class ModifyCircleService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CirclesMasterRepository circlesMasterRepository;

	public CommonResponse modify(ModifyCircleRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<CirclesMaster> circleOptional = circlesMasterRepository.findById(req.getId());

			if (!circleOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("Circle with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			CirclesMaster existingCircle = circlesMasterRepository.findByCircleNameOrCode(req.getCircleName(),
					req.getCode());

			if (existingCircle != null && !existingCircle.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Circle with the given name or code already exists.");
				response.setRespCode("02");
				return response;
			}

			CirclesMaster circle = circleOptional.get();
			circle.setCircleName(req.getCircleName());
			circle.setCode(req.getCode());
			circle.setCircleCode1(req.getCircleCode1());
			circle.setCircleCode2(req.getCircleCode2());
			circle.setCircleCode3(req.getCircleCode3());
			circle.setStatus(req.getStatus());
			circle.setModifyBy(req.getUsername().toUpperCase());
			circle.setModifyDt(new Date());

			circlesMasterRepository.save(circle);

			response.setStatus(true);
			response.setMessage("Circle modified successfully.");
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
