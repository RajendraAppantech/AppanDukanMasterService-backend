package com.appan.design.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.design.models.DesginReceiptCreateRequest;
import com.appan.entity.DesignReceiptsMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.DesignReceiptsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class DesginReceiptCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DesignReceiptsMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(DesginReceiptCreateService.class);

	public CommonResponse create(@Valid DesginReceiptCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			DesignReceiptsMaster existingRecord = repository.findByDesignAndCategory(req.getDesign().trim(),
					req.getCategory().trim());
			if (existingRecord != null) {
				response.setStatus(false);
				response.setMessage("Design and category combination already exists.");
				response.setRespCode("02");
				return response;
			}

			DesignReceiptsMaster newRecord = new DesignReceiptsMaster();
			newRecord.setDesign(req.getDesign());
			newRecord.setCategory(req.getCategory());
			newRecord.setParameter(req.getParameter());
			newRecord.setStatus(req.getStatus());
			newRecord.setReceiptTemplateBody(req.getReceiptTemplateBody());
			newRecord.setCreatedBy(req.getUsername().toUpperCase());
			newRecord.setCreatedDt(new Date());
			newRecord.setAuthStatus("4");

			repository.save(newRecord);

			response.setStatus(true);
			response.setMessage("Design receipt created successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating design receipt: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
