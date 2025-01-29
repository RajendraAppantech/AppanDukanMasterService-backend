package com.appan.notification.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SmsTemplateMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.SmsTemplateCreateRequest;
import com.appan.repositories.Repositories.SmsTemplateMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class SmsTemplateCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SmsTemplateMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(SmsTemplateCreateService.class);

	public CommonResponse create(@Valid SmsTemplateCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SmsTemplateMaster existingWallet = repository.findBySmsTypeAndTemplateIdIgnoreCase(req.getSmsType().trim(),
					req.getTemplateId().trim());
			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Sms Template with the same name and type already exists.");
				response.setRespCode("01");
				return response;
			}

			SmsTemplateMaster mst = new SmsTemplateMaster();
			mst.setSmsContent(req.getSmsContent());
			mst.setSmsType(req.getSmsType());
			mst.setTemplateId(req.getTemplateId());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Sms Template created successfully.");
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
