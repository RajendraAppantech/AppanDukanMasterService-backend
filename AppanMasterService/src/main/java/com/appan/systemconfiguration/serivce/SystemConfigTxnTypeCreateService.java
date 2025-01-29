package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigTxnType;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigTxnTypeCreateRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigTxnTypeRepository;

@Service
public class SystemConfigTxnTypeCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SystemConfigTxnTypeRepository repository;

	public CommonResponse create(SystemConfigTxnTypeCreateRequest req) {
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SystemConfigTxnType master = repository.findByCode(req.getCode().toUpperCase().trim());
			if (master != null) {
				response.setStatus(false);
				response.setMessage("Transaction code already exist");
				response.setRespCode("01");
				return response;
			}

			SystemConfigTxnType mst = new SystemConfigTxnType();
			mst.setCode(req.getCode().toUpperCase());
			mst.setTxnType(req.getTxnType());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Transaction type created successfully.");
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
