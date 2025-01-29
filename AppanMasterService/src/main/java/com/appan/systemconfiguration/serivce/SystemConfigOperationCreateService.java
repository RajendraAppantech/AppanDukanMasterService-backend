package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigOperation;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigOperationCreateRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigOperationRepository;

@Service
public class SystemConfigOperationCreateService {

	@Autowired
	private SystemConfigOperationRepository repository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse create(SystemConfigOperationCreateRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SystemConfigOperation master = repository.findByOperationTypeAndCode(req.getOperationType(), req.getCode());
			if (master != null) {
				response.setStatus(false);
				response.setMessage("Operation type already exist with code");
				response.setRespCode("01");
				return response;
			}

			SystemConfigOperation mst = new SystemConfigOperation();
			mst.setOperationType(req.getOperationType());
			mst.setOperationName(req.getOperationName());
			mst.setOperationAuth(req.getOperationAuth());
			mst.setUserType(req.getUserType());
			mst.setChannel(req.getChannel());
			mst.setDescription(req.getDescription());
			mst.setCode(req.getCode());
			mst.setIsOnlyUi(req.getIsOnlyUi());
			mst.setIsMakerChecker(req.getIsMakerChecker());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Operation created successfully.");
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
