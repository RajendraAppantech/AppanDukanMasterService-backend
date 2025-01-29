package com.appan.apimaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.CreateAndroidSettingMasterReq;
import com.appan.apimaster.repositories.ApiRepositories.AndroidSettingMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.AndroidSettingMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class AndroidSettingMasterCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private AndroidSettingMasterRepository androidSettingMasterRepository;

	public CommonResponse createAndroidSetting(@Valid CreateAndroidSettingMasterReq req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			AndroidSettingMaster existingSetting = androidSettingMasterRepository
					.findByTenantNameAndSenderId(req.getTenantName().trim(), req.getSenderId().trim());

			if (existingSetting != null) {
				response.setStatus(false);
				response.setMessage("Android Setting Master already exists for the given tenant and sender ID.");
				response.setRespCode("01");
				return response;
			}

			AndroidSettingMaster newSetting = new AndroidSettingMaster();
			newSetting.setTenantName(req.getTenantName());
			newSetting.setSenderId(req.getSenderId());
			newSetting.setServerKey(req.getServerKey());
			newSetting.setStatus(req.getStatus());
			newSetting.setCreatedBy(req.getUsername().toUpperCase());
			newSetting.setCreatedDt(new Date());
			newSetting.setAuthStatus("4");

			androidSettingMasterRepository.save(newSetting);

			response.setStatus(true);
			response.setMessage("Android Setting Master created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("An exception occurred while creating the Android Setting Master.");
			response.setRespCode("EX");
			return response;
		}

		return response;
	}
}
