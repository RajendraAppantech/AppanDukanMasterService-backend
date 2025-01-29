package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.documentattribute.model.ModifyDocumentAttributeRequest;
import com.appan.kycmaster.entity.DocumentAttributeMaster;
import com.appan.kycmaster.repositories.KycRepositories.DocumentAttributeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyDocumentAttributeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DocumentAttributeMasterRepository documentAttributeMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyDocumentAttributeService.class);

	public CommonResponse modify(@Valid ModifyDocumentAttributeRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			DocumentAttributeMaster existingAttribute = documentAttributeMasterRepository.findById(req.getId())
					.orElse(null);

			if (existingAttribute == null) {
				response.setStatus(false);
				response.setMessage("Document Attribute entry not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			DocumentAttributeMaster duplicateAttribute = documentAttributeMasterRepository
					.findByAttributeNameIgnoreCase(req.getAttributeName());

			if (duplicateAttribute != null && !duplicateAttribute.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Another Document Attribute entry with the same name already exists.");
				response.setRespCode("03");
				return response;
			}

			existingAttribute.setAttributeName(req.getAttributeName());
			existingAttribute.setDocumentName(req.getDocumentName());
			existingAttribute.setFieldType(req.getFieldType());
			existingAttribute.setIsEkyc(req.getIsEkyc());
			existingAttribute.setEkycCode(req.getEkycCode());
			existingAttribute.setLabel(req.getLabel());
			existingAttribute.setMaxSize(req.getMaxSize());
			existingAttribute.setMaxWidth(req.getMaxWidth());
			existingAttribute.setPriority(req.getPriority());
			existingAttribute.setMinWidth(req.getMinWidth());
			existingAttribute.setWebRegex(req.getWebRegex());
			existingAttribute.setMobileRegex(req.getMobileRegex());
			existingAttribute.setIsCameraAllowed(req.getIsCameraAllowed());
			existingAttribute.setIsUploadAllowed(req.getIsUploadAllowed());
			existingAttribute.setAttributeName(req.getAttributeName());
			existingAttribute.setStatus(req.getStatus());
			existingAttribute.setSupportedFileType(req.getSupportedFileType());
			existingAttribute.setModifyBy(req.getUsername().toUpperCase());
			existingAttribute.setModifyDt(new Date());

			documentAttributeMasterRepository.save(existingAttribute);

			response.setStatus(true);
			response.setMessage("Document Attribute modified successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
