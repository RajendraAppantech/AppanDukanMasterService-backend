package com.appan.usermanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.usermanagement.models.ModifyUserManagementRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;

import jakarta.validation.Valid;

@Service
public class AuthBlockUserManagementService {

	@Autowired
	private UserManagementMasterRepository userManagementMasterRepository;

	public CommonResponse authorblock(@Valid ModifyUserManagementRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

}
