package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.region.models.ModifyRegionRequest;
import com.appan.entity.RegionMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.RegionMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class ModifyRegionService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private RegionMasteRepository repository;

	public CommonResponse modify(ModifyRegionRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			RegionMaster existingRegion = repository.findById(req.getRegionId()).orElse(null);
			if (existingRegion == null) {
				response.setStatus(false);
				response.setMessage("Region not found.");
				response.setRespCode("01");
				return response;
			}

			existingRegion.setRegionType(req.getRegionType());
			existingRegion.setStatus(req.getStatus());
			existingRegion.setModifyBy(req.getUsername().toUpperCase());
			existingRegion.setModifyDt(new Date());
			repository.save(existingRegion);

			response.setStatus(true);
			response.setMessage("Region modified successfully.");
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
