package com.appan.businessmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.BusinessEntityType.model.BusinessEntityTypeCreateRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BusinessEntityType;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessEntityTypeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class BusinessEntityTypeCreateService {

	@Autowired
	private BusinessEntityTypeRepository businessEntityTypeRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(BusinessEntityTypeCreateService.class);

	public CommonResponse createBusinessEntityType(@Valid BusinessEntityTypeCreateRequest req) {
		Logger.info(
				"\r\n\r\n**************************** CREATE BUSINESS ENTITY TYPE *************************************");
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			String normalizedEntityTypeCode = req.getCode().toLowerCase();

			boolean entityExists = businessEntityTypeRepository.existsByCodeIgnoreCase(normalizedEntityTypeCode);

			if (entityExists) {
				response.setStatus(false);
				response.setMessage("Business Entity Type with this code already exists.");
				response.setRespCode("02");
				return response;
			}

			BusinessEntityType businessEntityType = new BusinessEntityType();
			businessEntityType.setEntityTypeName(req.getEntityTypeName());
			businessEntityType.setCode(req.getCode());
			businessEntityType.setIsKyc(req.getIsKyc());
			businessEntityType.setStatus(req.getStatus());
			businessEntityType.setCreatedBy(req.getUsername().toUpperCase());
			businessEntityType.setCreatedDt(new Date());
			businessEntityType.setAuthStatus("4");

			businessEntityTypeRepository.save(businessEntityType);

			response.setStatus(true);
			response.setMessage("Business Entity Type created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			Logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
		return response;
	}
}
