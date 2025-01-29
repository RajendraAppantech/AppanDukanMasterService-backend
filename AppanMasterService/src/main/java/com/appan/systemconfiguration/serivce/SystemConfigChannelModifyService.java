package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigChannel;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigChannelModifyRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigChannelRepository;

@Service
public class SystemConfigChannelModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SystemConfigChannelRepository repository;

	public CommonResponse modify(SystemConfigChannelModifyRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SystemConfigChannel mst = repository.findById(req.getSystemConfigChannelId()).orElse(null);
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("SystemConfigChannelId entry not found with ID: " + req.getSystemConfigChannelId());
				response.setRespCode("01");
				return response;
			}

			SystemConfigChannel existingMst = repository.findByChannelNameIgnoreCase(req.getChannelName());
			if (existingMst != null && !existingMst.getSystemConfigChannelId().equals(req.getSystemConfigChannelId())) {
				response.setStatus(false);
				response.setMessage("Duplicate channelName found.");
				response.setRespCode("02");
				return response;
			}


			mst.setChannelName(req.getChannelName());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());

			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Channel modified successfully.");
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
