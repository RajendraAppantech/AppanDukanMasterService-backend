package com.appan.notification.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.AlertTypeMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.AlertCreateRequest;
import com.appan.repositories.Repositories.AlertTypeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class AlertCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private AlertTypeMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(AlertCreateService.class);

	public CommonResponse create(@Valid AlertCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			AlertTypeMaster existingWallet = repository.findByAlertNameAndParametersNameIgnoreCase(
					req.getAlertName().trim(), req.getParametersName().trim());
			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Notification alert with the same name and parameter already exists.");
				response.setRespCode("01");
				return response;
			}

			AlertTypeMaster mst = new AlertTypeMaster();
			mst.setAlertName(req.getAlertName());
			mst.setParametersName(req.getParametersName());
			mst.setIsEmail(req.getIsEmail());
			mst.setIsSms(req.getIsSms());
			mst.setIsWhatsapp(req.getIsWhatsapp());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Notification alert created successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating wallet: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
