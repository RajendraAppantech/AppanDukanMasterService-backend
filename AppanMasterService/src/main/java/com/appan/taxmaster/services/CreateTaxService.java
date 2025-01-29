package com.appan.taxmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TaxMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TaxMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.taxmaster.model.CreateTaxRequest;

import jakarta.validation.Valid;

@Service
public class CreateTaxService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TaxMasterRepository taxMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(CreateTaxService.class);

	public CommonResponse create(@Valid CreateTaxRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			TaxMaster existingTax = taxMasterRepository.findByTaxTypeName(req.getTaxTypeName().trim().toUpperCase());

			if (existingTax != null) {
				response.setStatus(false);
				response.setMessage("Tax entry already exists for the given tax type name");
				response.setRespCode("01");
				return response;
			}

			TaxMaster newTax = new TaxMaster();
			newTax.setType(req.getType());
			newTax.setTaxTypeName(req.getTaxTypeName());
			newTax.setPrimaryTax(req.getPrimaryTax());
			newTax.setSecondaryTax(req.getSecondaryTax());
			newTax.setCreatedBy(req.getUsername().toUpperCase());
			newTax.setCreatedDt(new Date());
			newTax.setAuthStatus("4");

			try {
				taxMasterRepository.save(newTax);
			} catch (Exception e) {
				logger.error("Error while saving tax entry: ", e);
				response.setStatus(false);
				response.setMessage("An error occurred while saving the tax entry.");
				response.setRespCode("02");
				return response;
			}

			response.setStatus(true);
			response.setMessage("Tax entry created successfully");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Exception in create tax service: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
