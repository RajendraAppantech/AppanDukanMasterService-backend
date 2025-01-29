package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigOperationType;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigOperationTypeCreateRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigOperationTypeRepository;

@Service
public class SystemConfigOperationTypeCreateService {

	@Autowired
	private SystemConfigOperationTypeRepository repository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse create(SystemConfigOperationTypeCreateRequest req) {
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SystemConfigOperationType master = repository.findByOperationTypeIgnoreCase(req.getOperationType());
			if (master != null) {
				response.setStatus(false);
				response.setMessage("OperationType name already exist");
				response.setRespCode("01");
				return response;
			}

			SystemConfigOperationType mst = new SystemConfigOperationType();
			mst.setOperationType(req.getOperationType());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("OperationType created successfully.");
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
