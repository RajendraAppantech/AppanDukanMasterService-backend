package com.appan.paymentmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.PaymentModeMaster;
import com.appan.entity.UserMaster;
import com.appan.paymentmaster.model.PaymentModeAuthRequest;
import com.appan.repositories.Repositories.PaymentModeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class PaymentmodeAuthOrBlockService {

	private static final Logger Logger = LoggerFactory.getLogger(PaymentmodeAuthOrBlockService.class);

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private PaymentModeMasterRepository repository;

	public CommonResponse authorizeOrBlock(PaymentModeAuthRequest req) {
		Logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK PAYMENT MODE *************************************");
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			PaymentModeMaster paymentMode = repository.findById(req.getId()).orElse(null);
			if (paymentMode == null) {
				response.setStatus(false);
				response.setMessage("Payment Mode not found.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && paymentMode.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Payment Mode is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && paymentMode.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Payment Mode is already blocked.");
				response.setRespCode("01");
				return response;
			}

			paymentMode.setAuthBy(req.getUsername().toUpperCase());
			paymentMode.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				paymentMode.setAuthStatus("1");
				response.setMessage("Payment Mode authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				paymentMode.setAuthStatus("3");
				response.setMessage("Payment Mode blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid status.");
				return response;
			}

			repository.save(paymentMode);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			Logger.error("Exception occurred: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred.");
			response.setRespCode("EX");
			return response;
		}
	}
}
