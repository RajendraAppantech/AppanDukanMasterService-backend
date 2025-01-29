package com.appan.countrymaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.city.models.AuthOrBlockCityRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.CityMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CityMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthBlockCityService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CityMasteRepository cityMasteRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockCityService.class);

	public CommonResponse authorblock(AuthOrBlockCityRequest req) {
		logger.info("\r\n\r\n**************************** AUTH OR BLOCK CITY *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			CityMaster city = cityMasteRepository.findById(req.getCityId()).orElse(null);
			if (city == null) {
				response.setStatus(false);
				response.setMessage("City ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && city.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("City is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && city.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("City is already blocked.");
				response.setRespCode("01");
				return response;
			}

			city.setAuthBy(req.getUsername().toUpperCase());
			city.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				city.setAuthStatus("1");
				response.setMessage("City authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				city.setAuthStatus("3");
				response.setMessage("City blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			cityMasteRepository.save(city);

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
