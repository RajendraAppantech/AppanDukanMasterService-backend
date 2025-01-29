package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.documentattribute.model.AuthOrBlockDocumentAttributeRequest;
import com.appan.kycmaster.repositories.KycRepositories.DocumentAttributeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class AuthBlockDocumentAttributeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DocumentAttributeMasterRepository documentAttributeMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockDocumentAttributeService.class);

	public CommonResponse authOrBlock(@Valid AuthOrBlockDocumentAttributeRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK Document Attribute *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			var documentAttribute = documentAttributeMasterRepository.findById(req.getId()).orElse(null);
			if (documentAttribute == null) {
				response.setStatus(false);
				response.setMessage("Document attribute not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			String currentAuthStatus = documentAttribute.getAuthStatus();

			if ("1".equalsIgnoreCase(req.getStatus()) && "1".equalsIgnoreCase(currentAuthStatus)) {
				response.setStatus(false);
				response.setMessage("Document attribute is already authorized.");
				response.setRespCode("03");
				return response;
			}

			if ("3".equalsIgnoreCase(req.getStatus()) && "3".equalsIgnoreCase(currentAuthStatus)) {
				response.setStatus(false);
				response.setMessage("Document attribute is already blocked.");
				response.setRespCode("04");
				return response;
			}

			documentAttribute.setAuthStatus(req.getStatus());
			documentAttribute.setAuthBy(req.getUsername().toUpperCase());
			documentAttribute.setAuthDate(new Date());

			documentAttributeMasterRepository.save(documentAttribute);

			if ("1".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Document attribute successfully authorized.");
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Document attribute successfully blocked.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid status.");
				return response;
			}

			response.setStatus(true);
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
