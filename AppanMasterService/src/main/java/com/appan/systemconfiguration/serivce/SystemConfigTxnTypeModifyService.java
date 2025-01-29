package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigTxnType;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigTxnTypeModifyRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigTxnTypeRepository;

@Service
public class SystemConfigTxnTypeModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SystemConfigTxnTypeRepository repository;

	public CommonResponse modify(SystemConfigTxnTypeModifyRequest req) {
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SystemConfigTxnType mst = repository.findById(req.getSystemConfigTxnTypeId()).orElse(null);
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("SystemConfigTxnType entry not found with ID: " + req.getSystemConfigTxnTypeId());
				response.setRespCode("01");
				return response;
			}

			SystemConfigTxnType existingMst = repository.findByTxnTypeAndCode(req.getTxnType(), req.getCode());
			if (existingMst != null && !existingMst.getSystemConfigTxnTypeId().equals(req.getSystemConfigTxnTypeId())) {
				response.setStatus(false);
				response.setMessage("Duplicate txnType and code found.");
				response.setRespCode("02");
				return response;
			}

			mst.setTxnType(req.getTxnType());
			mst.setCode(req.getCode());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Transaction type modify successfully.");
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