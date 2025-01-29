package com.appan.countrymaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.country.models.AuthOrBlockCountryRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.CountryMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CountryMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthBlockCountryService {

	private static final Logger Logger = LoggerFactory.getLogger(AuthBlockCountryService.class);

	@Autowired
	private CountryMasteRepository countryMasteRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse authorblock(AuthOrBlockCountryRequest req) {
		Logger.info("\r\n\r\n**************************** AUTH OR BLOCK COUNTRY *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			CountryMaster country = countryMasteRepository.findById(req.getId()).orElse(null);
			if (country == null) {
				response.setStatus(false);
				response.setMessage("Country ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && country.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Country already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && country.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Country is already blocked.");
				response.setRespCode("01");
				return response;
			}

			country.setAuthBy(req.getUsername().toUpperCase());
			country.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				country.setAuthStatus("1");
				response.setMessage("Country authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				country.setAuthStatus("3");
				response.setMessage("Country blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			countryMasteRepository.save(country);

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
