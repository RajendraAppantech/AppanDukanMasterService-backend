package com.appan.serviceConfig.operatorUpdate.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigOperatorUpdateMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorUpdateRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operatorUpdate.model.OperatorUpdateCreateRequest;

import jakarta.validation.Valid;

@Service
public class OperatorUpdateCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorUpdateRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(OperatorUpdateCreateService.class);

	public CommonResponse create(@Valid OperatorUpdateCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorUpdateMaster existingWallet = repository.findByApiOperatorCode1AndOperatorNameIgnoreCase (req.getApiOperatorCode1().trim(), req.getOperatorName().trim());
			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Service OperatorUpdate with the same name and code already exists.");
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorUpdateMaster mst = new ServiceConfigOperatorUpdateMaster();
			mst.setOperatorName(req.getOperatorName());
			mst.setApiOperatorCode1(req.getApiOperatorCode1());
			mst.setApiOperatorCode2(req.getApiOperatorCode2());
			mst.setApiOperatorCode3(req.getApiOperatorCode3());
			mst.setApiOperatorCode4(req.getApiOperatorCode4());
			mst.setApiOperatorCode5(req.getApiOperatorCode5());
			mst.setApiOperatorCode6(req.getApiOperatorCode6());
			mst.setApiOperatorCode7(req.getApiOperatorCode7());
			mst.setApiOperatorCode8(req.getApiOperatorCode8());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service OperatorUpdate created successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating wallet: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
