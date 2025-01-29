package com.appan.countrymaster.services;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.blockpo.models.AuthOrBlockBlockPoRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BlockPoMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BlockPoMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AuthBlockBlockPoService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private BlockPoMasteRepository blockPoMasteRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockBlockPoService.class);

	public CommonResponse authorblock(AuthOrBlockBlockPoRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK BLOCK/PO *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<BlockPoMaster> blockPoMasterOptional = blockPoMasteRepository.findById(req.getBlockPoId());
			if (!blockPoMasterOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("Block PO with ID " + req.getBlockPoId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			BlockPoMaster blockPoMaster = blockPoMasterOptional.get();

			if (req.getStatus().equalsIgnoreCase("1") && blockPoMaster.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Block PO is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && blockPoMaster.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Block PO is already blocked.");
				response.setRespCode("01");
				return response;
			}

			blockPoMaster.setAuthBy(req.getUsername().toUpperCase());
			blockPoMaster.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				blockPoMaster.setAuthStatus("1");
				response.setMessage("Block PO authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				blockPoMaster.setAuthStatus("3");
				response.setMessage("Block PO blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			blockPoMasteRepository.save(blockPoMaster);

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
