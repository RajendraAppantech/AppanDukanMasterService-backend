package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.kycgroup.model.ModifyKycGroupRequest;
import com.appan.kycmaster.entity.KycGroupMaster;
import com.appan.kycmaster.repositories.KycRepositories.KycGroupMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyKycGroupService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private KycGroupMasterRepository kycGroupMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyKycGroupService.class);

	public CommonResponse modify(@Valid ModifyKycGroupRequest req) {
		logger.info("\r\n\r\n**************************** MODIFY KYC GROUP *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			KycGroupMaster existingGroup = kycGroupMasterRepository.findById(req.getId()).orElse(null);
			if (existingGroup == null) {
				response.setStatus(false);
				response.setMessage("KYC Group entry not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			KycGroupMaster duplicateGroup = kycGroupMasterRepository
					.findByKycGroupNameOrCodeIgnoreCase(req.getKycGroupName(), req.getCode());

			if (duplicateGroup != null && !duplicateGroup.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Another entry with the same KYC group name or code already exists.");
				response.setRespCode("03");
				return response;
			}

			existingGroup.setKycGroupName(req.getKycGroupName());
			existingGroup.setDescription(req.getDescription());
			existingGroup.setCode(req.getCode());
			existingGroup.setPriority(req.getPriority());
			existingGroup.setStatus(req.getStatus());
			existingGroup.setModifyBy(req.getUsername().toUpperCase());
			existingGroup.setModifyDt(new Date());

			kycGroupMasterRepository.save(existingGroup);

			response.setStatus(true);
			response.setMessage("KYC Group modified successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
