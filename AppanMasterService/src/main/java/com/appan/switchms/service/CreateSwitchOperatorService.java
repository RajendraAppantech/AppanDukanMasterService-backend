package com.appan.switchms.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SwitchOperatorMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.switchms.models.SwitchOpratorCreateRequest;
import com.appan.switchms.repositories.SwitchMsRepositories.SwitchOperatorMasterRepository;

@Service
public class CreateSwitchOperatorService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SwitchOperatorMasterRepository repository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchSwitchTypeService.class);

	public CommonResponse create(SwitchOpratorCreateRequest req) {
		CommonResponse response = new CommonResponse();
		Logger.info(
				"\r\n\r\n**************************** CREATE SWITCH OPERATOR *************************************");
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SwitchOperatorMaster master = repository.findByUserNameAndOperatorName(req.getUserName(),
					req.getOperatorName());
			if (master != null) {
				response.setStatus(false);
				response.setMessage("Switch operator already exists with the same username and operator name.");
				response.setRespCode("01");
				return response;
			}

			SwitchOperatorMaster mst = new SwitchOperatorMaster();
			mst.setUserName(req.getUserName());
			mst.setOperatorName(req.getOperatorName());
			mst.setApiCount(req.getApiCount());
			mst.setApiName1(req.getApiName1());
			mst.setApiName2(req.getApiName2());
			mst.setApiName3(req.getApiName3());
			mst.setApiName4(req.getApiName4());
			mst.setApiName5(req.getApiName5());
			mst.setApiName6(req.getApiName6());
			mst.setApiName7(req.getApiName7());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Switch operator created successfully.");
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
