package com.appan.serviceConfig.operator.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigOperatorMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operator.model.OperatorAuthOrBlockRequest;

import jakarta.validation.Valid;

@Service
public class OperatorAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(OperatorAuthOrBlockService.class);

	public CommonResponse authorblock(@Valid OperatorAuthOrBlockRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorMaster mst = repository.findById(req.getId()).orElse(null);
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Service Operator with the given ID not found.");
				response.setRespCode("01");
				return response;
			}

			String currentStatus = mst.getAuthStatus();
			if (req.getStatus().equalsIgnoreCase("1") && currentStatus.equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Service Operator is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && currentStatus.equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Service Operator is already blocked.");
				response.setRespCode("01");
				return response;
			}

			mst.setAuthBy(req.getUsername().toUpperCase());
			mst.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				mst.setAuthStatus("1"); 
				response.setMessage("Service Operator authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				mst.setAuthStatus("3"); 
				response.setMessage("Service Operator blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			repository.save(mst);
			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
}
}
