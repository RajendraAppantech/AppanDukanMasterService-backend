package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.document.model.ModifyDocumentRequest;
import com.appan.kycmaster.entity.DocumentMaster;
import com.appan.kycmaster.repositories.KycRepositories.DocumentMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyDocumentService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DocumentMasterRepository documentMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyDocumentService.class);

	public CommonResponse modify(@Valid ModifyDocumentRequest req) {
		logger.info("\r\n\r\n**************************** MODIFY DOCUMENT *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			DocumentMaster existingDocument = documentMasterRepository.findById(req.getId()).orElse(null);
			if (existingDocument == null) {
				response.setStatus(false);
				response.setMessage("Document entry not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			DocumentMaster duplicateDocument = documentMasterRepository
					.findByDocumentNameIgnoreCase(req.getDocumentName());

			if (duplicateDocument != null && !duplicateDocument.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Another entry with the same document name already exists (case-insensitive).");
				response.setRespCode("03");
				return response;
			}

			existingDocument.setDocumentName(req.getDocumentName());
			existingDocument.setHasExpiry(req.getHasExpiry());
			existingDocument.setIsEkyc(req.getIsEkyc());
			existingDocument.setPriority(req.getPriority());
			existingDocument.setIsMandatory(req.getIsMandatory());
			existingDocument.setStatus(req.getStatus());
			existingDocument.setModifyBy(req.getUsername().toUpperCase());
			existingDocument.setModifyDt(new Date());

			documentMasterRepository.save(existingDocument);

			response.setStatus(true);
			response.setMessage("Document modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred while modifying the document.");
			response.setRespCode("EX");
		}

		return response;
	}
}
