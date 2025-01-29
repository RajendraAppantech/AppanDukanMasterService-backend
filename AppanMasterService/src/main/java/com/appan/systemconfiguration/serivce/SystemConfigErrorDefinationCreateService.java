package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigErrorDefination;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigErrorDefinationCreateRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigErrorDefinationRepository;

@Service
public class SystemConfigErrorDefinationCreateService {

	@Autowired
	private SystemConfigErrorDefinationRepository repository;
	
	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse create(SystemConfigErrorDefinationCreateRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			
			SystemConfigErrorDefination master = repository.findByErrorCode(req.getErrorCode().toUpperCase());
			if (master != null) {
				response.setStatus(false);
				response.setMessage("ErrorDefination already exist");
				response.setRespCode("01");
				return response;
			}

			SystemConfigErrorDefination mst = new SystemConfigErrorDefination();
			mst.setErrorCode(req.getErrorCode().toUpperCase());
			mst.setErrorMessage(req.getErrorMessage());
			mst.setErrorType(req.getErrorType());
			mst.setErrorDesc(req.getErrorDesc());
			mst.setStatus(req.getStatus());
			mst.setResolution(req.getResolution());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("ErrorDefination created successfully.");
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
