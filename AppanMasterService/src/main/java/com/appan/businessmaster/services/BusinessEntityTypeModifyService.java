package com.appan.businessmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.BusinessEntityType.model.BusinessEntityTypeModifyRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BusinessEntityType;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessEntityTypeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class BusinessEntityTypeModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private BusinessEntityTypeRepository businessEntityTypeRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessEntityTypeModifyService.class);

	public CommonResponse modifyBusinessEntityType(@Valid BusinessEntityTypeModifyRequest req) {
		logger.info(
				"\r\n\r\n**************************** MODIFY BUSINESS ENTITY TYPE *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			BusinessEntityType existingEntityType = businessEntityTypeRepository.findById(req.getId()).orElse(null);
			if (existingEntityType == null) {
				response.setStatus(false);
				response.setMessage("Business entity type entry not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			BusinessEntityType duplicateEntityType = businessEntityTypeRepository
					.findByEntityTypeNameOrCode(req.getEntityTypeName(), req.getCode());

			if (duplicateEntityType != null && !duplicateEntityType.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Another entry with the same entity type name or code already exists.");
				response.setRespCode("03");
				return response;
			}

			existingEntityType.setEntityTypeName(req.getEntityTypeName());
			existingEntityType.setCode(req.getCode());
			existingEntityType.setIsKyc(req.getIsKyc());
			existingEntityType.setStatus(req.getStatus());
			existingEntityType.setModifyBy(req.getUsername().toUpperCase());
			existingEntityType.setModifyDt(new Date());

			businessEntityTypeRepository.save(existingEntityType);

			response.setStatus(true);
			response.setMessage("Business entity type modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}

}
