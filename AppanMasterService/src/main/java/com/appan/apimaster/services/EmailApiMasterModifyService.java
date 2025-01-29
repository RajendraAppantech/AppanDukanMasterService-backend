package com.appan.apimaster.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.apimaster.models.ModifyEmailApiMaster;
import com.appan.apimaster.repositories.ApiRepositories.EmailApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.EmailApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class EmailApiMasterModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private EmailApiMasterRepository emailApiMasterRepository;

	public CommonResponse modifyEmailApi(@Valid ModifyEmailApiMaster req) {
		CommonResponse response = new CommonResponse();

		try {
			// Check if the user exists
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			// Check if the entry exists by ID
			Optional<EmailApiMaster> emailApiMasterOptional = emailApiMasterRepository.findById(req.getId());
			if (!emailApiMasterOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("Entry with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			// Check if the data is already present before updating
			EmailApiMaster existingEmailApiMaster = emailApiMasterRepository.findByEmailAddress(req.getEmailAddress());
			if (existingEmailApiMaster != null && !existingEmailApiMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Email Address already exists");
				response.setRespCode("02");
				return response;
			}

			// Proceed with updating the record
			EmailApiMaster emailApiMaster = emailApiMasterOptional.get();
			emailApiMaster.setHostName(req.getHostName());
			emailApiMaster.setEmailAddress(req.getEmailAddress());
			emailApiMaster.setCc(req.getCc());
			emailApiMaster.setBcc(req.getBcc());
			emailApiMaster.setPort(req.getPort());
			emailApiMaster.setIsMailActive(req.getIsMailActive());
			emailApiMaster.setMailSsl(req.getMailSsl());

			emailApiMasterRepository.save(emailApiMaster);

			response.setStatus(true);
			response.setMessage("Email API modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("03");
			return response;
		}
	}
}
