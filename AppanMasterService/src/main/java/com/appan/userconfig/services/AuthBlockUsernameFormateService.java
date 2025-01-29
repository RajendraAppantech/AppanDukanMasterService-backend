package com.appan.userconfig.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UsernameFormat;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UsernameFormatRepository;
import com.appan.userconfig.models.AuthOrBlockUsernameFormateRequest;

@Service
public class AuthBlockUsernameFormateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UsernameFormatRepository usernameFormatRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthBlockUsernameFormateService.class);

	public CommonResponse authorblock(AuthOrBlockUsernameFormateRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			Optional<UsernameFormat> usernameFormat = usernameFormatRepository.findById(req.getId());
			if (!usernameFormat.isPresent()) {
				response.setStatus(false);
				response.setMessage("Menu ID not found");
				response.setRespCode("01");
				return response;
			}

			UsernameFormat format = usernameFormat.get();
			if (req.getStatus().equalsIgnoreCase("1") && format.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Entry is already authorized");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && format.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Entry is already blocked");
				response.setRespCode("01");
				return response;
			}

			format.setAuthBy(req.getUsername().toUpperCase());
			format.setAuthDt(new java.util.Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				format.setAuthStatus("1");
				response.setMessage("Entry authorized successfully");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				format.setAuthStatus("3");
				response.setMessage("Entry blocked successfully");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			usernameFormatRepository.save(format);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}
}
