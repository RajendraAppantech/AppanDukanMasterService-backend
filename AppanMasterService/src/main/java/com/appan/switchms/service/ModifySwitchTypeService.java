package com.appan.switchms.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SwitchTypeMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.switchms.models.ModifySwitchTypeRequest;
import com.appan.switchms.repositories.SwitchMsRepositories.SwitchTypeMasterRepository;

@Service
public class ModifySwitchTypeService {

	private static final Logger Logger = LoggerFactory.getLogger(FetchSwitchTypeService.class);

	@Autowired
	private SwitchTypeMasterRepository repository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse modify(ModifySwitchTypeRequest req) {
		Logger.info("\r\n\r\n**************************** MODIFY SWITCH TYPE *************************************");
		CommonResponse response = new CommonResponse();
		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			SwitchTypeMaster mst = repository.findById(req.getId()).orElse(null);
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Switch type with given ID not found");
				response.setRespCode("01");
				return response;
			}
			SwitchTypeMaster existing = repository.findBySwitchTypeAndCode(req.getSwitchType(), req.getCode());
			if (existing != null && existing.getId() != req.getId()) {
				response.setStatus(false);
				response.setMessage("Switch type with the same switchType and code already exists");
				response.setRespCode("02");
				return response;
			}

			mst.setSwitchType(req.getSwitchType());
			mst.setPriority(req.getPriority());
			mst.setCode(req.getCode());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());

			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Switch type modified successfully.");
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
