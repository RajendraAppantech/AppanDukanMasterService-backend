package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.ekyc.model.ModifyEkycRequest;
import com.appan.kycmaster.entity.EkycMaster;
import com.appan.kycmaster.repositories.KycRepositories.EkycMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyEkycService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private EkycMasterRepository ekycMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyEkycService.class);

	public CommonResponse modify(@Valid ModifyEkycRequest req) {
		logger.info("\r\n\r\n**************************** MODIFY EKYC *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			EkycMaster existingEkyc = ekycMasterRepository.findById(req.getId()).orElse(null);
			if (existingEkyc == null) {
				response.setStatus(false);
				response.setMessage("EKYC entry not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			EkycMaster duplicateEkyc = ekycMasterRepository.findByEkycNameIgnoreCaseOrCodeIgnoreCase(req.getEkycName(),
					req.getCode());

			if (duplicateEkyc != null && !duplicateEkyc.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Another entry with the same EKYC name or code already exists (case-insensitive).");
				response.setRespCode("03");
				return response;
			}

			existingEkyc.setEkycName(req.getEkycName());
			existingEkyc.setCode(req.getCode());
			existingEkyc.setType(req.getType());
			existingEkyc.setApiName(req.getApiName());
			existingEkyc.setStatus(req.getStatus());
			existingEkyc.setModifyBy(req.getUsername().toUpperCase());
			existingEkyc.setModifyDt(new Date());

			ekycMasterRepository.save(existingEkyc);

			response.setStatus(true);
			response.setMessage("EKYC modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred while modifying the EKYC.");
			response.setRespCode("EX");
		}

		return response;
	}
}
