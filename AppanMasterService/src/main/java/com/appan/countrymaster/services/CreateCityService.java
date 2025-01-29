package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.city.models.CreateCityRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.CityMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CityMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class CreateCityService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CityMasteRepository cityMasteRepository;

	public CommonResponse create(@Valid CreateCityRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			CityMaster cityMaster = cityMasteRepository.findByCityNameAndStateNameAndDistrictNameIgnoreCase(
					req.getCityName().trim(), req.getStateName().trim(), req.getDistrictName().trim());

			if (cityMaster != null) {
				response.setStatus(false);
				response.setMessage("City already exists in this district and state.");
				response.setRespCode("01");
				return response;
			}

			CityMaster newCity = new CityMaster();
			newCity.setCityName(req.getCityName());
			newCity.setDistrictName(req.getDistrictName());
			newCity.setStateName(req.getStateName());
			newCity.setStatus(req.getStatus());
			newCity.setAuthStatus("4");
			newCity.setCreatedBy(req.getUsername().toUpperCase());
			newCity.setCreatedDt(new Date());

			cityMasteRepository.save(newCity);

			response.setStatus(true);
			response.setMessage("City created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}

		return response;
	}
}
