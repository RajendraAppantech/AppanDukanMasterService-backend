package com.appan.userconfig.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UserTypeOperation;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserTypeOperationRepository;
import com.appan.userconfig.models.CreateUserTypeOpRequest;

import jakarta.validation.Valid;

@Service
public class CreateUserTypeOpService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserTypeOperationRepository userTypeOpRepository;

	private static final Logger logger = LoggerFactory.getLogger(CreateUserTypeOpService.class);

	public CommonResponse create(@Valid CreateUserTypeOpRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserTypeOperation existingOp = userTypeOpRepository.findByOperationNameAndUserTypeName(
					req.getOperationName().trim().toUpperCase(), req.getUserTypeName().trim().toUpperCase());

			if (existingOp != null) {
				response.setStatus(false);
				response.setMessage("Entry with the same operation name and user type already exists.");
				response.setRespCode("02");
				return response;
			}

			UserTypeOperation newOp = new UserTypeOperation();
			newOp.setOperationName(req.getOperationName());
			newOp.setUserTypeName(req.getUserTypeName());
			newOp.setStartDate(req.getStartDate());
			newOp.setEndDate(req.getEndDate());
			newOp.setIsDateValidity(req.getIsDateValidity());
			newOp.setStatus(req.getStatus());
			newOp.setCreatedBy(req.getUsername().toUpperCase());
			newOp.setCreatedDt(new Date());
			newOp.setAuthStatus("4");

			try {
				userTypeOpRepository.save(newOp);
			} catch (Exception e) {
				logger.error("Error while saving UserTypeOp entry: ", e);
				response.setStatus(false);
				response.setMessage("An error occurred while saving the entry.");
				response.setRespCode("03");
				return response;
			}

			response.setStatus(true);
			response.setMessage("Entry created successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Exception in create UserTypeOp service: ", e);
			response.setStatus(false);
			response.setMessage("An unexpected error occurred.");
			response.setRespCode("EX");
			return response;
		}
	}
}
