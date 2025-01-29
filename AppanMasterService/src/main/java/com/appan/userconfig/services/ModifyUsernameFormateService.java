package com.appan.userconfig.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UsernameFormat;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UsernameFormatRepository;
import com.appan.userconfig.models.ModifyUsernameFormateRequest;

import jakarta.validation.Valid;

@Service
public class ModifyUsernameFormateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UsernameFormatRepository usernameFormatRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyUsernameFormateService.class);

	public CommonResponse modify(@Valid ModifyUsernameFormateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UsernameFormat usernameFormat = usernameFormatRepository.findById(req.getId()).orElse(null);
			if (usernameFormat == null) {
				response.setStatus(false);
				response.setMessage("Username format entry with the given ID not found.");
				response.setRespCode("02");
				return response;
			}

			UsernameFormat existingFormat = usernameFormatRepository.findByUsernameFormatAndUserTypeAndPrefix(
					req.getUsernameFormat(), req.getUserType(), req.getPrefix());

			if (existingFormat != null && !existingFormat.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage(
						"A username format entry with the same format, user type, and prefix already exists.");
				response.setRespCode("03");
				return response;
			}

			usernameFormat.setUsernameFormat(req.getUsernameFormat());
			usernameFormat.setUserType(req.getUserType());
			usernameFormat.setPrefix(req.getPrefix());
			usernameFormat.setSuffix(req.getSuffix());
			usernameFormat.setStatus(req.getStatus());
			usernameFormat.setModifyBy(req.getUsername().toUpperCase());
			usernameFormat.setModifyDt(new Date());

			usernameFormatRepository.save(usernameFormat);

			response.setStatus(true);
			response.setMessage("Username format modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Error modifying username format: ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while modifying the username format.");
			response.setRespCode("03");
			return response;
		}
	}
}
