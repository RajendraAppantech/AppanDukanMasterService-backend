package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.document.model.AuthOrBlockDocumentRequest;
import com.appan.kycmaster.entity.DocumentMaster;
import com.appan.kycmaster.repositories.KycRepositories.DocumentMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class AuthBlockDocumentService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DocumentMasterRepository documentMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockDocumentService.class);

	public CommonResponse authOrBlock(@Valid AuthOrBlockDocumentRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK Document *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			DocumentMaster document = documentMasterRepository.findById(req.getId()).orElse(null);
			if (document == null) {
				response.setStatus(false);
				response.setMessage("Document not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			if ("1".equalsIgnoreCase(req.getStatus()) && "1".equalsIgnoreCase(document.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Document is already authorized.");
				response.setRespCode("03");
				return response;
			}

			if ("3".equalsIgnoreCase(req.getStatus()) && "3".equalsIgnoreCase(document.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Document is already blocked.");
				response.setRespCode("04");
				return response;
			}

			document.setAuthStatus(req.getStatus());
			document.setAuthBy(req.getUsername().toUpperCase());
			document.setAuthDate(new Date());

			documentMasterRepository.save(document);

			if ("1".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Document successfully authorized.");
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Document successfully blocked.");
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
