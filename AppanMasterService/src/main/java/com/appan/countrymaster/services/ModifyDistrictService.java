package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.district.models.ModifyDistrictRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.DistrictMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.DistrictMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class ModifyDistrictService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DistrictMasteRepository districtMasteRepository;

	public CommonResponse modify(ModifyDistrictRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			DistrictMaster existingDistrict = districtMasteRepository.findById(req.getDistrictId()).orElse(null);

			if (existingDistrict == null) {
				response.setStatus(false);
				response.setMessage("District ID not found");
				response.setRespCode("01");
				return response;
			}

			DistrictMaster duplicateDistrict = districtMasteRepository
					.findByDistrictNameAndStateNameIgnoreCase(req.getDistrictName(), req.getStateName());

			if (duplicateDistrict != null && !duplicateDistrict.getDistrictId().equals(req.getDistrictId())) {
				response.setStatus(false);
				response.setMessage("District with the same name and state already exists");
				response.setRespCode("02");
				return response;
			}

			existingDistrict.setDistrictName(req.getDistrictName());
			existingDistrict.setStateName(req.getStateName());
			existingDistrict.setStatus(req.getStatus());
			existingDistrict.setModifyBy(req.getUsername().toUpperCase());
			existingDistrict.setModifyDt(new Date());
			districtMasteRepository.save(existingDistrict);

			response.setStatus(true);
			response.setMessage("District modified successfully");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
