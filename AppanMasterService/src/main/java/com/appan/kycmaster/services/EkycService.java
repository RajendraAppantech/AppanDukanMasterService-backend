package com.appan.kycmaster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.ekyc.model.CreateEkycRequest;
import com.appan.kycmaster.entity.EkycMaster;
import com.appan.kycmaster.repositories.KycRepositories.EkycMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class EkycService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private EkycMasterRepository ekycMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(EkycService.class);

	public CommonResponse create(@Valid CreateEkycRequest req) {

		logger.info("\r\n\r\n**************************** CREATE EKYC DOCUMENT *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			String ekycCode = req.getCode().toLowerCase();

			boolean ekycExists = ekycMasterRepository.existsByCodeIgnoreCase(ekycCode);

			if (ekycExists) {
				response.setStatus(false);
				response.setMessage("Ekyc with this code already exists.");
				response.setRespCode("02");
				return response;
			}

			EkycMaster ekycMaster = new EkycMaster();
			ekycMaster.setEkycName(req.getEkycName());
			ekycMaster.setCode(req.getCode());
			ekycMaster.setType(req.getType());
			ekycMaster.setApiName(req.getApiName());
			ekycMaster.setStatus(req.getStatus());
			ekycMaster.setCreatedBy(req.getUsername().toUpperCase());
			ekycMaster.setCreatedDt(new java.util.Date());
			ekycMaster.setAuthStatus("4");

			ekycMasterRepository.save(ekycMaster);

			response.setStatus(true);
			response.setMessage("Ekyc created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}

		return response;
	}
}
