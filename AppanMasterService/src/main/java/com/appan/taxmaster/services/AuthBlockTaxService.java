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
import com.appan.taxmaster.model.AuthOrBlockTaxRequest;

@Service
public class AuthBlockTaxService {

	@Autowired
	private TaxMasterRepository taxMasterRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(AuthBlockTaxService.class);

	public CommonResponse authorblock(AuthOrBlockTaxRequest req) {
		Logger.info("\r\n\r\n**************************** AUTH OR BLOCK TAX *************************************");

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
				response.setMessage("Tax entry with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && taxMaster.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Tax entry is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && taxMaster.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Tax entry is already blocked.");
				response.setRespCode("01");
				return response;
			}

			taxMaster.setAuthBy(req.getUsername().toUpperCase());
			taxMaster.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				taxMaster.setAuthStatus("1");
				response.setMessage("Tax entry authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				taxMaster.setAuthStatus("3");
				response.setMessage("Tax entry blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			taxMasterRepository.save(taxMaster);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
