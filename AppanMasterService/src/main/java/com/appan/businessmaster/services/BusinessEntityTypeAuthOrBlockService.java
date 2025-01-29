package com.appan.businessmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.BusinessEntityType.model.BusinessEntityTypeAuthOrBlockRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BusinessEntityType;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessEntityTypeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class BusinessEntityTypeAuthOrBlockService {

	@Autowired
	private BusinessEntityTypeRepository businessEntityTypeRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessEntityTypeAuthOrBlockService.class);

	public CommonResponse authorBlockBusinessEntityType(@Valid BusinessEntityTypeAuthOrBlockRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK BusinessEntity *************************************");
		CommonResponse response = new CommonResponse();

		try {
			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			BusinessEntityType businessEntity = businessEntityTypeRepository.findById(req.getId()).orElse(null);
			if (businessEntity == null) {
				response.setStatus(false);
				response.setMessage("Business entity type not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			if ("1".equalsIgnoreCase(req.getStatus()) && "1".equalsIgnoreCase(businessEntity.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Business entity type is already authorized.");
				response.setRespCode("03");
				return response;
			}

			if ("3".equalsIgnoreCase(req.getStatus()) && "3".equalsIgnoreCase(businessEntity.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Business entity type is already blocked.");
				response.setRespCode("04");
				return response;
			}

			businessEntity.setAuthStatus(req.getStatus());
			businessEntity.setAuthBy(req.getUsername().toUpperCase());
			businessEntity.setAuthDate(new Date());

			businessEntityTypeRepository.save(businessEntity);

			if ("1".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Business entity type successfully authorized.");
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Business entity type successfully blocked.");
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
