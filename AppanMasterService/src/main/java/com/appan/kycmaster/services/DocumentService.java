package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.document.model.CreateDocumentRequest;
import com.appan.kycmaster.entity.DocumentMaster;
import com.appan.kycmaster.repositories.KycRepositories.DocumentMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class DocumentService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DocumentMasterRepository documentMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	public CommonResponse create(@Valid CreateDocumentRequest req) {

		logger.info("\r\n\r\n*************** CREATE DOCUMENT ***************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			boolean documentExists = documentMasterRepository.existsByDocumentNameIgnoreCase(req.getDocumentName());
			if (documentExists) {
				response.setStatus(false);
				response.setMessage("Document with this name already exists.");
				response.setRespCode("02");
				return response;
			}

			DocumentMaster documentMaster = new DocumentMaster();
			documentMaster.setDocumentName(req.getDocumentName());
			documentMaster.setHasExpiry(req.getHasExpiry());
			documentMaster.setIsEkyc(req.getIsEkyc());
			documentMaster.setPriority(req.getPriority());
			documentMaster.setIsMandatory(req.getIsMandatory());
			documentMaster.setStatus(req.getStatus());
			documentMaster.setCreatedBy(req.getUsername().toUpperCase());
			documentMaster.setCreatedDt(new Date());
			documentMaster.setAuthStatus("4");

			documentMasterRepository.save(documentMaster);

			response.setStatus(true);
			response.setMessage("Document created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
