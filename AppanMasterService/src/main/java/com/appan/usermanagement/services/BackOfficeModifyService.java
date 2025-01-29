package com.appan.usermanagement.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BackOfficeManagement;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.usermanagement.models.ModifyBackOfficeRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.BackOfficeManagementRepository;

import jakarta.validation.Valid;

@Service
public class BackOfficeModifyService {

	private static final Logger logger = LoggerFactory.getLogger(BackOfficeModifyService.class);

	@Autowired
	private BackOfficeManagementRepository repository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse modify(@Valid ModifyBackOfficeRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			BackOfficeManagement existingBackOffice = repository.findById(req.getId()).orElse(null);
			if (existingBackOffice == null) {
				response.setStatus(false);
				response.setMessage("User entry with the given ID not found.");
				response.setRespCode("02");
				return response;
			}

			//existingBackOffice.setUserId(req.getUserId());
			existingBackOffice.setUserType(req.getUserType());
			existingBackOffice.setName(req.getName());
			existingBackOffice.setMobile(req.getMobile());
			existingBackOffice.setEmail(req.getEmail());
			existingBackOffice.setAlternateMobile(req.getAlternateMobile());
			existingBackOffice.setCompanyName(req.getCompanyName());
			existingBackOffice.setStatus(req.getStatus());
			existingBackOffice.setModifyBy(req.getUsername().toUpperCase());
			existingBackOffice.setModifyDt(new Date());

			repository.save(existingBackOffice);

			response.setStatus(true);
			response.setMessage("User data modified successfully.");
			response.setRespCode("00");

			return response;

		} catch (Exception e) {
			logger.error("Error modifying user entry: ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while modifying the user entry.");
			response.setRespCode("03");
			return response;
		}
	}
}
