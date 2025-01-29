package com.appan.serviceConfig.operatorGrouping.services;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigOperatorGroupingMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorGroupingRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operatorGrouping.model.OperatorGroupingCreateRequest;

import jakarta.validation.Valid;

@Service
public class OperatorGroupingCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorGroupingRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(OperatorGroupingCreateService.class);

	public CommonResponse create(@Valid OperatorGroupingCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorGroupingMaster existingWallet = repository.findByGroupNameAndOperatorNameAndParameterNameIgnoreCase(req.getGroupName().trim() , req.getOperatorName().trim(), req.getParameterName().trim());
			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Service OperatorGrouping with the same Operator And Paramete name already exists.");
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorGroupingMaster mst = new ServiceConfigOperatorGroupingMaster();
			mst.setGroupName(req.getGroupName());
			mst.setOperatorName(req.getOperatorName());
			mst.setParameterName(req.getParameterName());
			mst.setValue(req.getValue());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service OperatorGrouping created successfully.");
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
