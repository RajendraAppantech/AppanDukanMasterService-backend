package com.appan.notification.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SendSmsEmailMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.SendSmsRequest;
import com.appan.repositories.Repositories.SendSmsEmailMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.validation.Valid;

@Service
public class SendSmsService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SendSmsEmailMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(SendSmsService.class);

	public CommonResponse sendSms(@Valid SendSmsRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			if (Strings.isNullOrEmpty(req.getMobileNo())) {
				response.setStatus(false);
				response.setMessage("Mobile No is required");
				response.setRespCode("01");
				return response;
			}

			processMobileNumbers(req.getMobileNo(), req);

			response.setStatus(true);
			response.setMessage("Sms request accepted successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating wallet: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}

	public CommonResponse bulkSms(@Valid SendSmsRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			if (Strings.isNullOrEmpty(req.getUserType())) {
				response.setStatus(false);
				response.setMessage("User Type is required");
				response.setRespCode("01");
				return response;
			}

			SendSmsEmailMaster mst = new SendSmsEmailMaster();
			mst.setSmsEmailType("SMS");
			mst.setUserMobileType("USER_TYPE");
			mst.setUserType(req.getUserType());
			mst.setApiName(req.getApiName());
			mst.setMessage(req.getMessage());
			mst.setSmsEmailBody(req.getSmsEmailBody());
			mst.setStatus("P");
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Bulk Sms request accepted successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating wallet: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}

	public void processMobileNumbers(String mobileNumbers, SendSmsRequest req) {
		// Split the input string by comma
		String[] numbersArray = mobileNumbers.split(",");

		for (String mobileNo : numbersArray) {
			mobileNo = mobileNo.trim();

			if (!mobileNo.isEmpty()) {
				SendSmsEmailMaster mst = new SendSmsEmailMaster();
				mst.setSmsEmailType("SMS");
				mst.setUserMobileType("MOBILE");
				//mst.setUserType(req.getUserType());
				mst.setApiName(req.getApiName());
				mst.setMessage(req.getMessage());
				mst.setSmsEmailBody(req.getSmsEmailBody());
				mst.setStatus("P");
				mst.setCreatedBy(req.getUsername().toUpperCase());
				mst.setCreatedDt(new Date());
				mst.setMobileNo(mobileNo); 
				repository.save(mst);
			}
		}
	}
}
