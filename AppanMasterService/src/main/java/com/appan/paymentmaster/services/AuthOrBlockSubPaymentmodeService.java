package com.appan.paymentmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SubPaymentMode;
import com.appan.entity.UserMaster;
import com.appan.paymentmaster.model.SubPaymentModeAuthRequest;
import com.appan.repositories.Repositories.SubPaymentModeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthOrBlockSubPaymentmodeService {

	private static final Logger logger = LoggerFactory.getLogger(AuthOrBlockSubPaymentmodeService.class);

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SubPaymentModeRepository subPaymentModeRepository;

	public CommonResponse authorizeOrBlock(SubPaymentModeAuthRequest req) {
		logger.info(
				"**************************** AUTH OR BLOCK SUB PAYMENT MODE *************************************");
		CommonResponse response = new CommonResponse();

		try {
			UserMaster userMaster = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (userMaster == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SubPaymentMode subpaymentModeMaster = subPaymentModeRepository.findById(req.getId()).orElse(null);
			if (subpaymentModeMaster == null) {
				response.setStatus(false);
				response.setMessage("Payment Mode ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && subpaymentModeMaster.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Payment Mode already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && subpaymentModeMaster.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Payment Mode already blocked.");
				response.setRespCode("01");
				return response;
			}

			subpaymentModeMaster.setAuthBy(req.getUsername().toUpperCase());
			subpaymentModeMaster.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				subpaymentModeMaster.setAuthStatus("1");
				response.setMessage("Payment Mode authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				subpaymentModeMaster.setAuthStatus("3");
				response.setMessage("Payment Mode blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			subPaymentModeRepository.save(subpaymentModeMaster);
			response.setStatus(true);
			response.setRespCode("00");
			return response;
		} catch (Exception e) {
			logger.error("Exception occurred during authorization or blocking of Payment Mode", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}
}
