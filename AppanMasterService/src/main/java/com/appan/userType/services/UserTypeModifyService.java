package com.appan.userType.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UserTypeMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserTypeMasterRepository;
import com.appan.userType.models.UserTypeModifyRequest;

import jakarta.validation.Valid;

@Service
public class UserTypeModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserTypeMasterRepository userTypeRepository;

	public CommonResponse modify(@Valid UserTypeModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid username");
				response.setRespCode("01");
				return response;
			}

			Optional<UserTypeMaster> existingEntry = userTypeRepository.findById(req.getMenuId());
			if (!existingEntry.isPresent()) {
				response.setStatus(false);
				response.setMessage("User type with the given menuId not found");
				response.setRespCode("02");
				return response;
			}

			UserTypeMaster userTypeMaster = existingEntry.get();

			userTypeMaster.setUserType(req.getUserType());
			userTypeMaster.setUserRank(req.getUserRank());
			userTypeMaster.setUserCode(req.getUserCode());
			userTypeMaster.setDescription(req.getDescription());
			userTypeMaster.setIsUser(req.getIsUser());
			userTypeMaster.setIsAllow(req.getIsAllow());
			userTypeMaster.setIsCommission(req.getIsCommission());
			userTypeMaster.setStatus(req.getStatus());

			userTypeMaster.setModifiedBy(req.getUsername().toUpperCase());
			userTypeMaster.setModifiedDate(new Date());

			userTypeRepository.save(userTypeMaster);

			response.setStatus(true);
			response.setMessage("User type modified successfully");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
