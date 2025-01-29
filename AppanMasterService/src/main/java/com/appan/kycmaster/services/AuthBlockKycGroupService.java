package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.kycgroup.model.AuthOrBlockKycGroupRequest;
import com.appan.kycmaster.entity.KycGroupMaster;
import com.appan.kycmaster.repositories.KycRepositories.KycGroupMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class AuthBlockKycGroupService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private KycGroupMasterRepository kycGroupMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockKycGroupService.class);

	public CommonResponse authOrBlock(@Valid AuthOrBlockKycGroupRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK KycGroup *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			KycGroupMaster kycGroup = kycGroupMasterRepository.findById(req.getId()).orElse(null);
			if (kycGroup == null) {
				response.setStatus(false);
				response.setMessage("Kyc Group not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			if ("1".equalsIgnoreCase(req.getStatus()) && "1".equalsIgnoreCase(kycGroup.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Kyc Group is already authorized.");
				response.setRespCode("03");
				return response;
			}

			if ("3".equalsIgnoreCase(req.getStatus()) && "3".equalsIgnoreCase(kycGroup.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Kyc Group is already blocked.");
				response.setRespCode("04");
				return response;
			}

			kycGroup.setAuthStatus(req.getStatus());
			kycGroup.setAuthBy(req.getUsername().toUpperCase());
			kycGroup.setAuthDate(new Date());
			kycGroupMasterRepository.save(kycGroup);

			if ("1".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Kyc Group successfully authorized.");
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Kyc Group successfully blocked.");
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
