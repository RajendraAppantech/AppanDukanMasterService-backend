package com.appan.apimaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.CreateEmailApiMaster;
import com.appan.apimaster.repositories.ApiRepositories.EmailApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.EmailApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class EmailApiMasterCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private EmailApiMasterRepository emailApiMasterRepository;

	public CommonResponse createEmailApi(@Valid CreateEmailApiMaster req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			EmailApiMaster existingEmailApi = emailApiMasterRepository
					.findByHostNameAndEmailAddressIgnoreCase(req.getHostName().trim(), req.getEmailAddress().trim());
			if (existingEmailApi != null) {
				response.setStatus(false);
				response.setMessage("Email API settings already exist for the given host and email address.");
				response.setRespCode("01");
				return response;
			}

			EmailApiMaster newEmailApi = new EmailApiMaster();
			newEmailApi.setHostName(req.getHostName().trim());
			newEmailApi.setEmailAddress(req.getEmailAddress().trim());
			newEmailApi.setCc(req.getCc());
			newEmailApi.setBcc(req.getBcc());
			newEmailApi.setPort(req.getPort());
			newEmailApi.setIsMailActive(req.getIsMailActive());
			newEmailApi.setMailSsl(req.getMailSsl());
			newEmailApi.setCreatedBy(req.getUsername().toUpperCase());
			newEmailApi.setCreatedDt(new Date());
			newEmailApi.setAuthStatus("4");

			emailApiMasterRepository.save(newEmailApi);

			response.setStatus(true);
			response.setMessage("Email API settings created successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("An exception occurred while creating the Email API settings.");
			response.setRespCode("EX");
		}

		return response;
	}
}
