package com.appan.countrymaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.pincode.models.AuthOrBlockPincodeRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.PincodeMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.PincodeMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthBlockPincodeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private PincodeMasteRepository pincodeMasteRepository;

	private static final Logger Logger = LoggerFactory.getLogger(AuthBlockPincodeService.class);

	public CommonResponse authorblock(AuthOrBlockPincodeRequest req) {
		Logger.info("\r\n\r\n**************************** AUTH OR BLOCK PINCODE *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			PincodeMaster pincode = pincodeMasteRepository.findById(req.getPincodeId()).orElse(null);
			if (pincode == null) {
				response.setStatus(false);
				response.setMessage("Pincode ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && pincode.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Pincode already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && pincode.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Pincode is already blocked.");
				response.setRespCode("01");
				return response;
			}

			pincode.setAuthBy(req.getUsername().toUpperCase());
			pincode.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				pincode.setAuthStatus("1");
				response.setMessage("Pincode authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				pincode.setAuthStatus("3");
				response.setMessage("Pincode blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			pincodeMasteRepository.save(pincode);

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
