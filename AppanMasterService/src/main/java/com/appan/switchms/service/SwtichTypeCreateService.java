package com.appan.switchms.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SwitchTypeMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.switchms.models.SwitchTypeCreateRequest;
import com.appan.switchms.repositories.SwitchMsRepositories.SwitchTypeMasterRepository;

@Service
public class SwtichTypeCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SwitchTypeMasterRepository repository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchSwitchTypeService.class);

	public CommonResponse create(SwitchTypeCreateRequest req) {
		CommonResponse response = new CommonResponse();
		Logger.info("\r\n\r\n**************************** CREATE SWITCH TYPE *************************************");
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			SwitchTypeMaster master = repository.findBySwitchType(req.getSwitchType());
			if (master != null) {
				response.setStatus(false);
				response.setMessage("Switch type already exist");
				response.setRespCode("01");
				return response;
			}

			SwitchTypeMaster mst = new SwitchTypeMaster();
			mst.setSwitchType(req.getSwitchType());
			mst.setPriority(req.getPriority());
			mst.setCode(req.getCode());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Switch Type created successfully.");
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
