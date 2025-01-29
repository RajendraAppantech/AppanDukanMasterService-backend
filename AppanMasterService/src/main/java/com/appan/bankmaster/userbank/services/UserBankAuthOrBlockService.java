package com.appan.bankmaster.userbank.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.bankmaster.entity.UserBank;
import com.appan.bankmaster.userbank.model.UserBankAuthOrBlockRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.repositories.Repositories.UserBankRepository;

@Service
public class UserBankAuthOrBlockService {

	private static final Logger logger = LoggerFactory.getLogger(UserBankAuthOrBlockService.class);

	@Autowired
	private UserBankRepository userBankRepository;

	public CommonResponse authorBlockUserBank(UserBankAuthOrBlockRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserBank userBank = userBankRepository.findById(req.getId()).orElse(null);
			if (userBank == null) {
				response.setStatus(false);
				response.setMessage("UserBank entry with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && userBank.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("UserBank entry is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && userBank.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("UserBank entry is already blocked.");
				response.setRespCode("01");
				return response;
			}

			userBank.setAuthBy(req.getUsername().toUpperCase());
			userBank.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				userBank.setAuthStatus("1");
				response.setMessage("UserBank entry authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				userBank.setAuthStatus("3");
				response.setMessage("UserBank entry blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			userBankRepository.save(userBank);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
