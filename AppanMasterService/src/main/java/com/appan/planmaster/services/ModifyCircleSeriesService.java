package com.appan.planmaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.planmaster.circleseries.model.ModifyCircleSeriesRequest;
import com.appan.planmaster.entity.CircleSeriesMaster;
import com.appan.planmaster.repositories.PlanMasterRepositories.CircleSeriesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class ModifyCircleSeriesService {
	
	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CircleSeriesMasterRepository circleSeriesMasterRepository;

	public CommonResponse modify(ModifyCircleSeriesRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<CircleSeriesMaster> circleSeriesOptional = circleSeriesMasterRepository.findById(req.getId());

			if (!circleSeriesOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("Circle Series with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			CircleSeriesMaster existingCircleSeries = circleSeriesMasterRepository
					.findByNumberPrefixAndCircleNameAndOperatorName(req.getNumberPrefix(), req.getCircleName(),
							req.getOperatorName());

			if (existingCircleSeries != null && !existingCircleSeries.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage(
						"Circle Series with the given number prefix, circle name, and operator name already exists");
				response.setRespCode("02");
				return response;
			}

			CircleSeriesMaster circleSeries = circleSeriesOptional.get();
			circleSeries.setNumberPrefix(req.getNumberPrefix());
			circleSeries.setCircleName(req.getCircleName());
			circleSeries.setOperatorName(req.getOperatorName());
			circleSeries.setStatus(req.getStatus());
			circleSeries.setIsUpdate(req.getIsUpdate());
			circleSeries.setIsCalledForUpdate(req.getIsCalledForUpdate());
			circleSeries.setModifyBy(req.getUsername().toUpperCase());
			circleSeries.setModifyDt(new Date());

			circleSeriesMasterRepository.save(circleSeries);

			response.setStatus(true);
			response.setMessage("Circle Series modified successfully.");
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
