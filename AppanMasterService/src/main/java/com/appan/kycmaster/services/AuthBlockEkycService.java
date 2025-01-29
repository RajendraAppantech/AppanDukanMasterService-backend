package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.ekyc.model.AuthOrBlockEkycRequest;
import com.appan.kycmaster.entity.EkycMaster;
import com.appan.kycmaster.repositories.KycRepositories.EkycMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class AuthBlockEkycService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private EkycMasterRepository ekycMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockEkycService.class);

	public CommonResponse authOrBlock(@Valid AuthOrBlockEkycRequest req) {
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			EkycMaster ekycMaster = ekycMasterRepository.findById(req.getId()).orElse(null);
			if (ekycMaster == null) {
				response.setStatus(false);
				response.setMessage("Ekyc entry not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			if ("1".equalsIgnoreCase(req.getStatus()) && "1".equalsIgnoreCase(ekycMaster.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Ekyc entry is already authorized.");
				response.setRespCode("03");
				return response;
			}

			if ("3".equalsIgnoreCase(req.getStatus()) && "3".equalsIgnoreCase(ekycMaster.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Ekyc entry is already blocked.");
				response.setRespCode("04");
				return response;
			}

			ekycMaster.setAuthStatus(req.getStatus());
			ekycMaster.setAuthBy(req.getUsername().toUpperCase());
			ekycMaster.setAuthDate(new Date());

			ekycMasterRepository.save(ekycMaster);

			if ("1".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Ekyc entry successfully authorized.");
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Ekyc entry successfully blocked.");
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
