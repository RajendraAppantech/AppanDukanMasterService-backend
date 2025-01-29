package com.appan.usermanagement.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserManagementMaster;
import com.appan.entity.UserTypeMaster;
import com.appan.repositories.Repositories.UserTypeMasterRepository;
import com.appan.usermanagement.models.GetParentDetailsResponse;
import com.appan.usermanagement.models.ParentDetailModel;
import com.appan.usermanagement.models.UserManagementFetchRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;
import com.google.common.base.Strings;

import jakarta.validation.Valid;

@Service
public class GetParentDetailsService {

	@Autowired
	private UserManagementMasterRepository repository;

	@Autowired
	private UserTypeMasterRepository userTypeMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(GetParentDetailsService.class);

	public GetParentDetailsResponse getParentDetails(UserManagementFetchRequest req) {
		logger.info("Fetching all user type data without filters");
		try {
			List<ParentDetailModel> parentDetails = new ArrayList<>();

			// Use below for live
			if (Strings.isNullOrEmpty(req.getUserType())) {
				return buildResponse(false, "user type cannot be null/Empty.", "01", null, null);
			}
			// Fetch all user types
			UserTypeMaster userTypeMasters = userTypeMasterRepository.findByUserType(req.getUserType());
			if (userTypeMasters == null) {
				return buildResponse(false, "No user types found with rank.", "02", null, null);
			}

			List<String> filteredUserTypes = userTypeMasterRepository.findAll().stream()
					.filter(userType -> userType.getUserRank() < userTypeMasters.getUserRank())
					.map(UserTypeMaster::getUserType).collect(Collectors.toList());

			if (filteredUserTypes.isEmpty()) {
				ParentDetailModel ct = new ParentDetailModel();
				ct.setUserId("Administrator");
				ct.setFullName("Administrator");
				ct.setUserType("Administrator");
				parentDetails.add(ct);
				return buildResponse(true, "SUCCESS", "00", parentDetails, null);
			}

			logger.info("filteredUserTypes : " + filteredUserTypes);

			List<UserManagementMaster> userTypeList = repository.findByUserTypeIn(filteredUserTypes);
			if (userTypeList.isEmpty()) {
				return buildResponse(false, "No user type records found.", "01", null, null);
			}

			parentDetails = userTypeList.stream().map(result -> {
				ParentDetailModel ct1 = new ParentDetailModel();
				ct1.setUserId(result.getUserId());
				ct1.setFullName(result.getFullName());
				ct1.setUserType(result.getUserType());
				return ct1;
			}).collect(Collectors.toList());
			return buildResponse(true, "SUCCESS", "00", parentDetails, null);

		} catch (Exception e) {
			logger.error("Exception in getAllData service: ", e);
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private GetParentDetailsResponse buildResponse(boolean status, String message, String respCode,
			List<ParentDetailModel> data, PageDetails pageDetails) {
		GetParentDetailsResponse response = new GetParentDetailsResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		return response;
	}

	public GetParentDetailsResponse getUserType(@Valid UserManagementFetchRequest req) {
		logger.info("Fetching all user type data without filters");
		try {
			List<ParentDetailModel> parentDetails = new ArrayList<>();

			// Use below for live
			if (Strings.isNullOrEmpty(req.getUserType())) {
				return buildResponse(false, "user type cannot be null/Empty.", "01", null, null);
			}
			// Fetch all user types
			UserTypeMaster userTypeMasters = userTypeMasterRepository.findByUserType(req.getUserType());
			if (userTypeMasters == null) {
				return buildResponse(false, "No user types found with rank.", "02", null, null);
			}

			List<UserTypeMaster> filteredUserTypes = userTypeMasterRepository.findAll().stream()
					.filter(userType -> userType.getUserRank() > userTypeMasters.getUserRank())
					.collect(Collectors.toList());

			if (filteredUserTypes.isEmpty()) {
				return buildResponse(false, "No user types found.", "02", null, null);
			}

			for (int i = 0; i < filteredUserTypes.size(); i++) {
				ParentDetailModel ctt = new ParentDetailModel();
				ctt.setUserType(filteredUserTypes.get(i).getUserType());
				parentDetails.add(ctt);
			}

			return buildResponse(true, "SUCCESS", "00", parentDetails, null);

		} catch (Exception e) {
			logger.error("Exception in getAllData service: ", e);
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}