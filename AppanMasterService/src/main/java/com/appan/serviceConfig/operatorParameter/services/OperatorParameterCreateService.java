package com.appan.serviceConfig.operatorParameter.services;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigOperatorParameterMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorParameterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operatorParameter.model.OperatorParameterCreateRequest;

import jakarta.validation.Valid;

@Service
public class OperatorParameterCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorParameterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(OperatorParameterCreateService.class);

	public CommonResponse create(@Valid OperatorParameterCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorParameterMaster existingWallet = repository.findByParameterNameAndOperatorNameIgnoreCase(req.getParameterName().trim() , req.getOperatorName().trim());
			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Service OperatorParameter with the same name already exists.");
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorParameterMaster mst = new ServiceConfigOperatorParameterMaster();
			mst.setOperatorName(req.getOperatorName());
			mst.setParameterName(req.getParameterName());
			mst.setMinLength(req.getMinLenght());
			mst.setMaxLength(req.getMaxLenght());
			mst.setFieldType(req.getFieldType());
			mst.setSort(req.getSort());
			mst.setManualSort(req.getManualSort());
			mst.setPattern(req.getPattern());
			mst.setApiName(req.getApiName());
			mst.setIsActive(req.getIsActive());
			mst.setIsMandatory(req.getIsMandatory());
			mst.setHasGrouping(req.getHasGrouping());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service OperatorParameter created successfully.");
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
