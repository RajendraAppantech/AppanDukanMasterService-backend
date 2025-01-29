package com.appan.registration.fields.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.RegistrationFields;
import com.appan.entity.UserMaster;
import com.appan.registration.fields.model.FieldsCreateRequest;
import com.appan.repositories.Repositories.FieldsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class FieldsCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private FieldsMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(FieldsCreateService.class);

	public CommonResponse create(@Valid FieldsCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			RegistrationFields existingWallet = repository.findByFieldsNameAndFieldsTypeIgnoreCase(req.getFieldsName().trim(), req.getEntityType().trim());
			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Registation Fields with the same name and Fields type already exists.");
				response.setRespCode("01");
				return response;
			}

			RegistrationFields ms = new RegistrationFields();
			ms.setFieldsName(req.getFieldsName());
			ms.setFieldsType(req.getFieldsType());
			ms.setStepsName(req.getStepsName());
			ms.setLabel(req.getLabel());
			ms.setUserType(req.getUserType());
			ms.setEntityType(req.getEntityType());
			ms.setRank(req.getRank());
			ms.setWebRegex(req.getWebRegex());
			ms.setMobileRegex(req.getMobileRegex());
			ms.setIsMandatory(req.getIsMandatory());
			ms.setIsSignup(req.getIsSignup());
			ms.setIsGroup(req.getIsGroup());
			ms.setHasCaps(req.getHasCaps());
			ms.setIsVerified(req.getIsVerified());
			ms.setIsDocumentGroup(req.getIsDocumentGroup());
			ms.setValidationMessage(req.getValidationMessage());
			ms.setGroupType(req.getGroupType());
			ms.setDocumentGroup(req.getDocumentGroup());
			ms.setMinLength(req.getMinLength());
			ms.setMaxLength(req.getMaxLength());
			ms.setCreatedBy(req.getUsername().toUpperCase());
			ms.setStatus(req.getStatus());
			ms.setCreatedDt(new Date());
			ms.setAuthStatus("4");
			repository.save(ms);

			response.setStatus(true);
			response.setMessage("Registation Fields created successfully.");
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
