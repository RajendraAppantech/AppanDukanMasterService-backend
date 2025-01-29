package com.appan.countrymaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.blockpo.models.ModifyBlockPoRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BlockPoMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BlockPoMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyBlockPoService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private BlockPoMasteRepository blockPoMasteRepository;

	public CommonResponse modify(@Valid ModifyBlockPoRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<BlockPoMaster> existingBlockPo = blockPoMasteRepository.findById(req.getBlockPoId());
			if (!existingBlockPo.isPresent()) {
				response.setStatus(false);
				response.setMessage("Block PO with ID " + req.getBlockPoId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			if (blockPoMasteRepository.existsByBlockPoNameAndCityNameIgnoreCase(req.getBlockPoName(),
					req.getCityName())) {
				response.setStatus(false);
				response.setMessage("Block PO with the name '" + req.getBlockPoName() + "' and city '"
						+ req.getCityName() + "' already exists.");
				response.setRespCode("02");
				return response;
			}

			BlockPoMaster blockPo = existingBlockPo.get();
			blockPo.setBlockPoName(req.getBlockPoName());
			blockPo.setCityName(req.getCityName());
			blockPo.setModifyBy(req.getUsername().toUpperCase());
			blockPo.setModifyDt(new Date());

			blockPoMasteRepository.save(blockPo);

			response.setStatus(true);
			response.setMessage("Block PO updated successfully.");
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
