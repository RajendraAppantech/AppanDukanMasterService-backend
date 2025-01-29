package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigChannel;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigChannelCreateRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigChannelRepository;

@Service
public class SystemConfigChannelCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SystemConfigChannelRepository repository;

	public CommonResponse create(SystemConfigChannelCreateRequest req) {
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SystemConfigChannel master = repository.findByChannelName(req.getChannelName());
			if (master != null) {
				response.setStatus(false);
				response.setMessage("Channel name already exist");
				response.setRespCode("01");
				return response;
			}

			SystemConfigChannel mst = new SystemConfigChannel();
			mst.setChannelName(req.getChannelName());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Channel created successfully.");
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
