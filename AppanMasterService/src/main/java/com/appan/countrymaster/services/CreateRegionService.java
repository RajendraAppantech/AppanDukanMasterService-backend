package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.region.models.CreateRegionRequest;
import com.appan.entity.RegionMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.RegionMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class CreateRegionService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private RegionMasteRepository repository;

	public CommonResponse create(CreateRegionRequest req) {
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			String regionName = req.getRegionName().toUpperCase().trim();
			String regionType = req.getRegionType().toUpperCase().trim();

			RegionMaster master = repository.findByRegionNameAndRegionType(regionName, regionType);
			if (master != null) {
				response.setStatus(false);
				response.setMessage("Region name with region type already exists");
				response.setRespCode("01");
				return response;
			}
			RegionMaster mst = new RegionMaster();
			mst.setRegionName(req.getRegionName());
			mst.setRegionType(req.getRegionType());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Region created successfully.");
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
