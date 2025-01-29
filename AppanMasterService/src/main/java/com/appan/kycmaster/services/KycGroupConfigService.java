package com.appan.kycmaster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.kycgroupconfig.model.CreateKycGroupConfigRequest;
import com.appan.kycmaster.entity.KycGroupConfigMaster;
import com.appan.kycmaster.repositories.KycRepositories.KycGroupConfigMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class KycGroupConfigService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private KycGroupConfigMasterRepository kycGroupConfigMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(KycGroupConfigService.class);

	public CommonResponse create(@Valid CreateKycGroupConfigRequest req) {

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			String documentName = req.getDocumentName().toLowerCase();
			String documentGroupName = req.getDocumentGroupName().toLowerCase();

			boolean entryExists = kycGroupConfigMasterRepository
					.existsByDocumentNameIgnoreCaseAndDocumentGroupNameIgnoreCase(documentName, documentGroupName);

			if (entryExists) {
				response.setStatus(false);
				response.setMessage("Configuration for this document name and group already exists.");
				response.setRespCode("02");
				return response;
			}

			KycGroupConfigMaster kycGroupConfig = new KycGroupConfigMaster();
			kycGroupConfig.setDocumentName(req.getDocumentName());
			kycGroupConfig.setDocumentGroupName(req.getDocumentGroupName());
			kycGroupConfig.setStatus(req.getStatus());
			kycGroupConfig.setCreatedBy(req.getUsername().toUpperCase());
			kycGroupConfig.setCreatedDt(new java.util.Date());
			kycGroupConfig.setAuthStatus("4");

			kycGroupConfigMasterRepository.save(kycGroupConfig);

			// Step 3: Prepare success response
			response.setStatus(true);
			response.setMessage("KYC Group Configuration created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
