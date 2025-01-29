package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.kycgroupconfig.model.ModifyKycGroupConfigRequest;
import com.appan.kycmaster.entity.KycGroupConfigMaster;
import com.appan.kycmaster.repositories.KycRepositories.KycGroupConfigMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyKycGroupConfigService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private KycGroupConfigMasterRepository kycGroupConfigMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyKycGroupConfigService.class);

	public CommonResponse modify(@Valid ModifyKycGroupConfigRequest req) {
		logger.info(
				"\r\n\r\n**************************** MODIFY KYC GROUP CONFIG *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			KycGroupConfigMaster existingConfig = kycGroupConfigMasterRepository.findById(req.getId()).orElse(null);
			if (existingConfig == null) {
				response.setStatus(false);
				response.setMessage("KYC Group Config entry not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			KycGroupConfigMaster duplicateConfig = kycGroupConfigMasterRepository
					.findByDocumentNameOrDocumentGroupNameIgnoreCase(req.getDocumentName(), req.getDocumentGroupName());

			if (duplicateConfig != null && !duplicateConfig.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Another entry with the same document name or group name already exists.");
				response.setRespCode("03");
				return response;
			}

			existingConfig.setDocumentName(req.getDocumentName());
			existingConfig.setDocumentGroupName(req.getDocumentGroupName());
			existingConfig.setStatus(req.getStatus());
			existingConfig.setModifyBy(req.getUsername().toUpperCase());
			existingConfig.setModifyDt(new Date());

			kycGroupConfigMasterRepository.save(existingConfig);

			response.setStatus(true);
			response.setMessage("KYC Group Config modified successfully.");
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
