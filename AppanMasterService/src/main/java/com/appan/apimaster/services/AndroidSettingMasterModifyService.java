package com.appan.apimaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.ModifyAndroidSettingMasterReq;
import com.appan.apimaster.repositories.ApiRepositories.AndroidSettingMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.AndroidSettingMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AndroidSettingMasterModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private AndroidSettingMasterRepository androidSettingMasterRepository;

	public CommonResponse modifyAndroidSetting(ModifyAndroidSettingMasterReq req) {
		CommonResponse response = new CommonResponse();

		try {
			// 1. Authenticate username
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// 2. Check if the entry exists in the table by ID
			Optional<AndroidSettingMaster> settingOptional = androidSettingMasterRepository.findById(req.getId());
			if (!settingOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("Android setting with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			AndroidSettingMaster existingSetting = settingOptional.get();

			// 3. Check if the data to be updated is already available in the table
			AndroidSettingMaster settingWithSameData = androidSettingMasterRepository
					.findByTenantNameAndSenderIdAndServerKey(req.getTenantName(), req.getSenderId(),
							req.getServerKey());

			if (settingWithSameData != null && !settingWithSameData.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Android setting with the same tenant, sender ID, and server key already exists");
				response.setRespCode("02");
				return response;
			}

			// 4. Modify the data
			existingSetting.setTenantName(req.getTenantName());
			existingSetting.setSenderId(req.getSenderId());
			existingSetting.setServerKey(req.getServerKey());
			existingSetting.setStatus(req.getStatus());
			existingSetting.setModifyBy(req.getUsername().toUpperCase());
			existingSetting.setModifyDt(new Date());

			androidSettingMasterRepository.save(existingSetting);

			// 5. Set the response
			response.setStatus(true);
			response.setMessage("Android setting modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
