package com.appan.planmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.planmaster.circleseries.model.AuthOrBlockCircleSeriesRequest;
import com.appan.planmaster.entity.CircleSeriesMaster;
import com.appan.planmaster.repositories.PlanMasterRepositories.CircleSeriesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthBlockCircleSeriesService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CircleSeriesMasterRepository circleSeriesMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockCircleSeriesService.class);

	public CommonResponse authorblock(AuthOrBlockCircleSeriesRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK CIRCLE SERIES *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			CircleSeriesMaster circleSeries = circleSeriesMasterRepository.findById(req.getId()).orElse(null);
			if (circleSeries == null) {
				response.setStatus(false);
				response.setMessage("Circle Series ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && circleSeries.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Circle Series is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && circleSeries.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Circle Series is already blocked.");
				response.setRespCode("01");
				return response;
			}

			circleSeries.setAuthBy(req.getUsername().toUpperCase());
			circleSeries.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				circleSeries.setAuthStatus("1");
				response.setMessage("Circle Series authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				circleSeries.setAuthStatus("3");
				response.setMessage("Circle Series blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			circleSeriesMasterRepository.save(circleSeries);

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
