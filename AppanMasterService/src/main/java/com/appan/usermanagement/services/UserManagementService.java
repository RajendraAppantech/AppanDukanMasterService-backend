package com.appan.usermanagement.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.appan.entity.UserTypeMaster;
import com.appan.repositories.Repositories.UserTypeMasterRepository;
import com.appan.usermanagement.models.GetParentDetailsResponse;
import com.appan.usermanagement.models.ParentDetailModel;
import com.appan.usermanagement.models.UserManagementFetchRequest;
import com.google.common.base.Strings;

import jakarta.validation.Valid;

@Service
@Validated
public class UserManagementService {

	private static final Logger logger = LoggerFactory.getLogger(UserManagementService.class);

	private final UserTypeMasterRepository userTypeMasterRepository;

	public UserManagementService(UserTypeMasterRepository userTypeMasterRepository) {
		this.userTypeMasterRepository = userTypeMasterRepository;
	}

	public GetParentDetailsResponse getLowerRankedUserTypes(@Valid UserManagementFetchRequest req) {
		logger.info("Fetching all user types lower than the given rank");

		try {
			List<ParentDetailModel> parentDetails = new ArrayList<>();

			// Validate input
			if (Strings.isNullOrEmpty(req.getUserType())) {
				return buildResponse(false, "User type cannot be null or empty.", "01", null, null);
			}

			// Fetch the user type from the database
			UserTypeMaster userTypeMasters = userTypeMasterRepository.findByUserType(req.getUserType());
			if (userTypeMasters == null) {
				return buildResponse(false, "No user types found with the given rank.", "02", null, null);
			}

			// Fetch all user types lower than the given rank
			List<UserTypeMaster> lowerRankedUserTypes = userTypeMasterRepository
					.findLowerRankedUserTypes(userTypeMasters.getUserRank());

			// Handle case where no data is found
			if (lowerRankedUserTypes.isEmpty()) {
				return buildResponse(false, "No lower-ranked user types found.", "02", null, null);
			}

			// Map to response model
			for (UserTypeMaster userType : lowerRankedUserTypes) {
				ParentDetailModel ctt = new ParentDetailModel();
				ctt.setUserType(userType.getUserType());
				parentDetails.add(ctt);
			}

			return buildResponse(true, "SUCCESS", "00", parentDetails, null);

		} catch (Exception e) {
			logger.error("Exception in fetching lower-ranked user types: ", e);
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private GetParentDetailsResponse buildResponse(boolean status, String message, String respCode,
			List<ParentDetailModel> data, Object extraData) {
		GetParentDetailsResponse response = new GetParentDetailsResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		return response;
	}
}
