package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.documentattribute.model.CreateDocumentAttributeRequest;
import com.appan.kycmaster.entity.DocumentAttributeMaster;
import com.appan.kycmaster.repositories.KycRepositories.DocumentAttributeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class DocumentAttributeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DocumentAttributeMasterRepository documentAttributeMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(DocumentAttributeService.class);

	public CommonResponse create(@Valid CreateDocumentAttributeRequest req) {
		logger.info("Starting creation of Document Attribute");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			DocumentAttributeMaster newAttribute = new DocumentAttributeMaster();
			newAttribute.setAttributeName(req.getAttributeName());
			newAttribute.setDocumentName(req.getDocumentName());
			newAttribute.setFieldType(req.getFieldType());
			newAttribute.setIsEkyc(req.getIsEkyc());
			newAttribute.setEkycCode(req.getEkycCode());
			newAttribute.setLabel(req.getLabel());
			newAttribute.setMaxSize(req.getMaxSize());
			newAttribute.setMaxWidth(req.getMaxWidth());
			newAttribute.setPriority(req.getPriority());
			newAttribute.setMinWidth(req.getMinWidth());
			newAttribute.setWebRegex(req.getWebRegex());
			newAttribute.setMobileRegex(req.getMobileRegex());
			newAttribute.setIsCameraAllowed(req.getIsCameraAllowed());
			newAttribute.setIsUploadAllowed(req.getIsUploadAllowed());
			newAttribute.setStatus(req.getStatus());
			newAttribute.setSupportedFileType(req.getSupportedFileType());
			newAttribute.setAttributeName(req.getAttributeName());
			newAttribute.setCreatedBy(req.getUsername().toUpperCase());
			newAttribute.setCreatedDt(new Date());
			newAttribute.setAuthStatus("4");

			documentAttributeMasterRepository.save(newAttribute);

			response.setStatus(true);
			response.setMessage("Document Attribute created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("Exception occurred while creating Document Attribute", e);
			response.setStatus(false);
			response.setMessage("An error occurred. Please try again.");
			response.setRespCode("EX");
		}

		return response;
	}
}
