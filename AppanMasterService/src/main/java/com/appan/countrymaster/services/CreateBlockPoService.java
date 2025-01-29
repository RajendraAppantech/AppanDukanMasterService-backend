package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.blockpo.models.CreateBlockPoRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BlockPoMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BlockPoMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class CreateBlockPoService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private BlockPoMasteRepository blockPoMasteRepository;

	public CommonResponse create(CreateBlockPoRequest req) {
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			BlockPoMaster existingBlockPo = blockPoMasteRepository.findByBlockPoName(req.getBlockPoName().trim());

			if (existingBlockPo != null) {
				response.setStatus(false);
				response.setMessage("Block PO name already exists");
				response.setRespCode("01");
				return response;
			}

			BlockPoMaster newBlockPo = new BlockPoMaster();
			newBlockPo.setBlockPoName(req.getBlockPoName());
			newBlockPo.setCityName(req.getCityName());
			newBlockPo.setStatus(req.getStatus());
			newBlockPo.setAuthStatus("4");
			newBlockPo.setCreatedBy(req.getUsername().toUpperCase());
			newBlockPo.setCreatedDt(new Date());

			blockPoMasteRepository.save(newBlockPo);

			response.setStatus(true);
			response.setMessage("Block PO created successfully.");
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
