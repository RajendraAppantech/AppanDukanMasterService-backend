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
import com.appan.userconfig.models.ModifyUserTypeOpRequest;

@Service
public class ModifyUserTypeOpService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserTypeOperationRepository userTypeOpRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyUserTypeOpService.class);

	public CommonResponse modify(ModifyUserTypeOpRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME); 
				response.setRespCode("01");
				return response;
			}

			UserTypeOperation userTypeOp = userTypeOpRepository.findById(req.getId()).orElse(null);
			if (userTypeOp == null) {
				response.setStatus(false);
				response.setMessage("User type operation entry with the given ID not found.");
				response.setRespCode("02");
				return response;
			}

			UserTypeOperation existingUserTypeOp = userTypeOpRepository
					.findByOperationNameAndUserTypeName(req.getOperationName(), req.getUserTypeName());

			if (existingUserTypeOp != null && !existingUserTypeOp.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage(
						"A user type operation entry with the same operation name and user type name already exists.");
				response.setRespCode("03");
				return response;
			}

			userTypeOp.setOperationName(req.getOperationName());
			userTypeOp.setUserTypeName(req.getUserTypeName());
			userTypeOp.setStartDate(req.getStartDate());
			userTypeOp.setEndDate(req.getEndDate());
			userTypeOp.setIsDateValidity(req.getIsDateValidity());
			userTypeOp.setStatus(req.getStatus());
			userTypeOp.setModifyBy(req.getUsername().toUpperCase());
			userTypeOp.setModifyDt(new Date());

			userTypeOpRepository.save(userTypeOp);

			response.setStatus(true);
			response.setMessage("User type operation modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Error modifying user type operation: ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while modifying the user type operation.");
			response.setRespCode("03");
			return response;
		}
	}
}
