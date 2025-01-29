package com.appan.countrymaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.city.models.ModifyCityRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.CityMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CityMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class ModifyCityService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CityMasteRepository cityMasteRepository;

	public CommonResponse modify(ModifyCityRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<CityMaster> cityOptional = cityMasteRepository.findById(req.getCityId());

			if (!cityOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("City with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			CityMaster existingCity = cityMasteRepository.findByCityNameAndDistrictNameAndStateName(req.getCityName(),
					req.getDistrictName(), req.getStateName());

			if (existingCity != null && !existingCity.getCityId().equals(req.getCityId())) {
				response.setStatus(false);
				response.setMessage("City with the given name already exists in this district and state");
				response.setRespCode("02");
				return response;
			}

			CityMaster city = cityOptional.get();
			city.setCityName(req.getCityName());
			city.setDistrictName(req.getDistrictName());
			city.setStateName(req.getStateName());
			city.setStatus(req.getStatus());
			city.setModifyBy(req.getUsername().toUpperCase());
			city.setModifyDt(new Date());

			cityMasteRepository.save(city);

			response.setStatus(true);
			response.setMessage("City modified successfully.");
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
