package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.district.models.CreateDistrictRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.DistrictMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.DistrictMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class CreateDistrictService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DistrictMasteRepository districtMasteRepository;

	public CommonResponse create(@Valid CreateDistrictRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			DistrictMaster districtMaster = districtMasteRepository.findByDistrictNameAndStateName(
					req.getDistrictName().toUpperCase().trim(), req.getStateName().toUpperCase().trim());

			if (districtMaster != null) {
				response.setStatus(false);
				response.setMessage("District already exists in this state.");
				response.setRespCode("01");
				return response;
			}

			DistrictMaster newDistrict = new DistrictMaster();
			newDistrict.setDistrictName(req.getDistrictName());
			newDistrict.setStateName(req.getStateName());
			newDistrict.setStatus(req.getStatus());
			newDistrict.setAuthStatus("4");
			newDistrict.setCreatedBy(req.getUsername().toUpperCase());
			newDistrict.setCreatedDt(new Date());

			districtMasteRepository.save(newDistrict);

			response.setStatus(true);
			response.setMessage("District created successfully.");
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
