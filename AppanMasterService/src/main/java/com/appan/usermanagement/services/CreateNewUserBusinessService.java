package com.appan.usermanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserManagementMaster;
import com.appan.usermanagement.models.CreateNewUserBusinessDetailsRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;

import jakarta.validation.Valid;

@Service
public class CreateNewUserBusinessService {

	@Autowired
	private UserManagementMasterRepository userManagementMasterRepository;

	public CommonResponse create(@Valid CreateNewUserBusinessDetailsRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserManagementMaster existingUser = userManagementMasterRepository.findByUserId(req.getUserId());
			if (existingUser == null) {
				response.setStatus(false);
				response.setMessage("User with the given userId does not exist.");
				response.setRespCode("01");
				return response;
			}

			if (req.getFirmShopName() != null) {
				existingUser.setFirmShopName(req.getFirmShopName());
			}
			if (req.getBusinessAddress() != null) {
				existingUser.setBusinessAddress(req.getBusinessAddress());
			}

			userManagementMasterRepository.save(existingUser);

			response.setStatus(true);
			response.setMessage("User business details updated successfully.");
			response.setRespCode("00");
			response.setData("userDetails",existingUser);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("An error occurred while updating user details.");
			response.setRespCode("03");
			return response;
		}
	}
}
