package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigErrorDefination;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigErrorDefinationModifyRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigErrorDefinationRepository;

@Service
public class SystemConfigErrorDefinationModifyService {

	@Autowired
	private SystemConfigErrorDefinationRepository repository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse modify(SystemConfigErrorDefinationModifyRequest req) {
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SystemConfigErrorDefination mst = repository.findById(req.getSystemConfigErrorDefinitionId()).orElse(null);
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Error definition id not found");
				response.setRespCode("01");
				return response;
			}

			SystemConfigErrorDefination existingEntry = repository.findByErrorCode(req.getErrorCode().toUpperCase());
			if (existingEntry != null && !existingEntry.getSystemConfigErrorDefinitionId()
					.equals(req.getSystemConfigErrorDefinitionId())) {
				response.setStatus(false);
				response.setMessage("Error code already exists.");
				response.setRespCode("02");
				return response;
			}

			mst.setErrorCode(req.getErrorCode().toUpperCase());
			mst.setErrorMessage(req.getErrorMessage());
			mst.setErrorType(req.getErrorType());
			mst.setErrorDesc(req.getErrorDesc());
			mst.setStatus(req.getStatus());
			mst.setResolution(req.getResolution());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Error defination modify successfully.");
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