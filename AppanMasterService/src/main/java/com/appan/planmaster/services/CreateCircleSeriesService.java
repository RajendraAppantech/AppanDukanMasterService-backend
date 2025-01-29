package com.appan.planmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.planmaster.circleseries.model.CreateCircleSeriesRequest;
import com.appan.planmaster.entity.CircleSeriesMaster;
import com.appan.planmaster.repositories.PlanMasterRepositories.CircleSeriesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class CreateCircleSeriesService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CircleSeriesMasterRepository circleSeriesMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(CreateCircleSeriesService.class);

	public CommonResponse create(@Valid CreateCircleSeriesRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			CircleSeriesMaster existingCircleSeries = circleSeriesMasterRepository
					.findByNumberPrefixAndCircleNameAndOperatorName(req.getNumberPrefix().trim(),
							req.getCircleName().trim(), req.getOperatorName().trim());

			if (existingCircleSeries != null) {
				response.setStatus(false);
				response.setMessage("Circle series already exists with the same details.");
				response.setRespCode("01");
				return response;
			}

			CircleSeriesMaster newCircleSeries = new CircleSeriesMaster();
			newCircleSeries.setNumberPrefix(req.getNumberPrefix());
			newCircleSeries.setCircleName(req.getCircleName());
			newCircleSeries.setOperatorName(req.getOperatorName());
			newCircleSeries.setStatus(req.getStatus());
			newCircleSeries.setIsCalledForUpdate(req.getIsCalledForUpdate());
			newCircleSeries.setIsUpdate(req.getIsUpdate());
			newCircleSeries.setCreatedBy(req.getUsername().toUpperCase());
			newCircleSeries.setCreatedDt(new Date());
			newCircleSeries.setAuthStatus("4");

			circleSeriesMasterRepository.save(newCircleSeries);

			response.setStatus(true);
			response.setMessage("Circle Series created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("Exception while creating Circle Series: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred while creating Circle Series.");
			response.setRespCode("EX");
		}

		return response;
	}
}
