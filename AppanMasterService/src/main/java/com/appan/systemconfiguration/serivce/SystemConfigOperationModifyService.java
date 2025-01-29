package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigOperation;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigOperationModifyRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigOperationRepository;

@Service
public class SystemConfigOperationModifyService {

	@Autowired
	private SystemConfigOperationRepository repository;
	
	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse modify(SystemConfigOperationModifyRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			
			SystemConfigOperation mst = repository.findBySystemConfigOperationId(req.getSystemConfigOperationId());
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Operation id not found");
				response.setRespCode("01");
				return response;
			}

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
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Operation modify successfully.");
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