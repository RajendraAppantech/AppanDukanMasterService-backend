package com.appan.kycmaster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.kycgroupconfig.model.AuthOrBlockKycGroupConfigRequest;
import com.appan.kycmaster.entity.KycGroupConfigMaster;
import com.appan.kycmaster.repositories.KycRepositories.KycGroupConfigMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class AuthBlockKycGroupConfigService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private KycGroupConfigMasterRepository kycGroupConfigMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockKycGroupConfigService.class);

	public CommonResponse authOrBlock(@Valid AuthOrBlockKycGroupConfigRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			KycGroupConfigMaster kycGroupConfig = kycGroupConfigMasterRepository.findById(req.getId()).orElse(null);
			if (kycGroupConfig == null) {
				response.setStatus(false);
				response.setMessage("KYC Group Config not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			if ("1".equalsIgnoreCase(req.getStatus()) && "1".equalsIgnoreCase(kycGroupConfig.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("KYC Group Config is already authorized.");
				response.setRespCode("03");
				return response;
			}

			if ("3".equalsIgnoreCase(req.getStatus()) && "3".equalsIgnoreCase(kycGroupConfig.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("KYC Group Config is already blocked.");
				response.setRespCode("04");
				return response;
			}

			kycGroupConfig.setAuthStatus(req.getStatus());
			kycGroupConfig.setModifyBy(req.getUsername().toUpperCase());
			kycGroupConfig.setModifyDt(new java.util.Date());

			kycGroupConfigMasterRepository.save(kycGroupConfig);

			if ("1".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("KYC Group Config successfully authorized.");
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("KYC Group Config successfully blocked.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid status.");
				return response;
			}

			response.setStatus(true);
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
