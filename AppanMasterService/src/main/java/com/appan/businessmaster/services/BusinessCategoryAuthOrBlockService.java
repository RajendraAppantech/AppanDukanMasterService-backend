package com.appan.businessmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.BusinessCategory.model.BusinessCategoryAuthOrBlockRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BusinessCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class BusinessCategoryAuthOrBlockService {

	@Autowired
	private BusinessCategoryRepository businessCategoryRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessCategoryAuthOrBlockService.class);

	public CommonResponse authorBlockBusinessCategory(@Valid BusinessCategoryAuthOrBlockRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK BusinessCategory *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			BusinessCategory businessCategory = businessCategoryRepository.findById(req.getId()).orElse(null);
			if (businessCategory == null) {
				response.setStatus(false);
				response.setMessage("Business category not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			if ("1".equalsIgnoreCase(req.getStatus()) && "1".equalsIgnoreCase(businessCategory.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Business category is already authorized.");
				response.setRespCode("03");
				return response;
			}

			if ("3".equalsIgnoreCase(req.getStatus()) && "3".equalsIgnoreCase(businessCategory.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Business category is already blocked.");
				response.setRespCode("04");
				return response;
			}

			businessCategory.setAuthStatus(req.getStatus());
			businessCategory.setAuthBy(req.getUsername().toUpperCase());
			businessCategory.setAuthDate(new Date());

			businessCategoryRepository.save(businessCategory);

			if ("1".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Business category successfully authorized.");
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Business category successfully blocked.");
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
