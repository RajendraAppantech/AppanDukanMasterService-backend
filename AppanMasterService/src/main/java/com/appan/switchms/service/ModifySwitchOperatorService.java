package com.appan.switchms.service;

import java.util.Date;
import java.util.Optional; // Import for Optional

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SwitchOperatorMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.switchms.models.ModifySwitchOpratorRequest;
import com.appan.switchms.repositories.SwitchMsRepositories.SwitchOperatorMasterRepository;

@Service
public class ModifySwitchOperatorService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SwitchOperatorMasterRepository repository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchSwitchTypeService.class);

	public CommonResponse modify(ModifySwitchOpratorRequest req) {
		Logger.info(
				"\r\n\r\n**************************** MODIFY SWITCH OPERATOR *************************************");
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			Optional<SwitchOperatorMaster> optionalMst = repository.findById(req.getId());
			if (!optionalMst.isPresent()) {
				response.setStatus(false);
				response.setMessage("Switch operator not found");
				response.setRespCode("01");
				return response;
			}

			SwitchOperatorMaster mst = optionalMst.get();
			mst.setUserName(req.getUserName());
			mst.setOperatorName(req.getOperatorName());
			mst.setApiCount(req.getApiCount());
			mst.setApiName1(req.getApiName1());
			mst.setApiName2(req.getApiName2());
			mst.setApiName3(req.getApiName3());
			mst.setApiName4(req.getApiName4());
			mst.setApiName5(req.getApiName5());
			mst.setApiName6(req.getApiName6());
			mst.setApiName7(req.getApiName7());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Switch operator modified successfully.");
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
