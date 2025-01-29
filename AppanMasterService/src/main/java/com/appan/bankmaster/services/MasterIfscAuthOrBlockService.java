package com.appan.bankmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.entity.MasterIfsc;
import com.appan.bankmaster.masterifsc.model.MasterIfscAuthOrBlockRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.MasterIfscRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class MasterIfscAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private MasterIfscRepository masterIfscRepository;

	private static final Logger Logger = LoggerFactory.getLogger(MasterIfscAuthOrBlockService.class);

	public CommonResponse authorBlockMasterIfsc(@Valid MasterIfscAuthOrBlockRequest req) {
		Logger.info("**************************** AUTH OR BLOCK MASTER IFSC *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			MasterIfsc ifsc = masterIfscRepository.findById(req.getId()).orElse(null);
			if (ifsc == null) {
				response.setStatus(false);
				response.setMessage("IFSC ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && ifsc.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("IFSC already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && ifsc.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("IFSC is already blocked.");
				response.setRespCode("01");
				return response;
			}

			ifsc.setAuthBy(req.getUsername().toUpperCase());
			ifsc.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				ifsc.setAuthStatus("1");
				response.setMessage("IFSC authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				ifsc.setAuthStatus("3");
				response.setMessage("IFSC blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			masterIfscRepository.save(ifsc);

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
