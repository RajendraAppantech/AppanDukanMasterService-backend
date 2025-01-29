package com.appan.walletmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SourceOfFund;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.SourceOfFundRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.walletmaster.model.SourceOfFundAuthOrBlockRequest;

@Service
public class SourceOfFundAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SourceOfFundRepository sourceOfFundRepository;

	private static final Logger logger = LoggerFactory.getLogger(SourceOfFundAuthOrBlockService.class);

	public CommonResponse authorBlockSourceOfFund(SourceOfFundAuthOrBlockRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK SOURCE OF FUND *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SourceOfFund sourceOfFund = sourceOfFundRepository.findById(req.getId()).orElse(null);
			if (sourceOfFund == null) {
				response.setStatus(false);
				response.setMessage("Source of Fund ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && sourceOfFund.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Source of Fund is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && sourceOfFund.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Source of Fund is already blocked.");
				response.setRespCode("01");
				return response;
			}

			sourceOfFund.setAuthBy(req.getUsername().toUpperCase());
			sourceOfFund.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				sourceOfFund.setAuthStatus("1");
				response.setMessage("Source of Fund authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				sourceOfFund.setAuthStatus("3");
				response.setMessage("Source of Fund blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			sourceOfFundRepository.save(sourceOfFund);

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
