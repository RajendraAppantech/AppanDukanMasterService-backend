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
import com.appan.taxmaster.model.ModifyTaxRequest;

@Service
public class ModifyTaxService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TaxMasterRepository taxMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyTaxService.class);

	public CommonResponse modify(ModifyTaxRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			TaxMaster taxMaster = taxMasterRepository.findById(req.getId()).orElse(null);

			if (taxMaster == null) {
				response.setStatus(false);
				response.setMessage("Tax entry with the given ID not found.");
				response.setRespCode("01");
				return response;
			}

			TaxMaster existingTax = taxMasterRepository.findByTaxTypeName(req.getTaxTypeName());

			if (existingTax != null && !existingTax.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("A tax entry with the same tax type name already exists.");
				response.setRespCode("03");
				return response;
			}

			taxMaster.setTaxTypeName(req.getTaxTypeName());
			taxMaster.setPrimaryTax(req.getPrimaryTax());
			taxMaster.setSecondaryTax(req.getSecondaryTax());
			taxMaster.setModifyBy(req.getUsername().toUpperCase());
			taxMaster.setModifyDt(new Date());

			taxMasterRepository.save(taxMaster);

			response.setStatus(true);
			response.setMessage("Tax entry modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Error modifying tax entry: ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while modifying the tax entry.");
			response.setRespCode("03");
			return response;
		}
	}
}
