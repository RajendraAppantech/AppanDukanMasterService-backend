package com.appan.notification.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.EmailTemplateMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.EmailTemplateCreateRequest;
import com.appan.repositories.Repositories.EmailTemplateMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class EmailTemplateCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private EmailTemplateMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(EmailTemplateCreateService.class);

	public CommonResponse create(@Valid EmailTemplateCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			EmailTemplateMaster existingWallet = repository
					.findByEmailTypeAndSubjectIgnoreCase(req.getEmailType().trim(), req.getSubject().trim());
			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Email Template with the same name and type already exists.");
				response.setRespCode("01");
				return response;
			}

			EmailTemplateMaster mst = new EmailTemplateMaster();
			mst.setEmailType(req.getEmailType());
			mst.setSubject(req.getSubject());
			mst.setEmailBody(req.getEmailBody());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Email Template created successfully.");
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
