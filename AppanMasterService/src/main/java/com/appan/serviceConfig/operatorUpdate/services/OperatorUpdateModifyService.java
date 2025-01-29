package com.appan.serviceConfig.operatorUpdate.services;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigOperatorUpdateMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorUpdateRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operatorUpdate.model.OperatorUpdateModifyRequest;

@Service
public class OperatorUpdateModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorUpdateRepository repository;

	public CommonResponse modify(OperatorUpdateModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<ServiceConfigOperatorUpdateMaster> msts = repository.findById(req.getId());
			if (!msts.isPresent()) {
				response.setStatus(false);
				response.setMessage("Service OperatorUpdate with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorUpdateMaster existingServiceConfigOperatorUpdateMaster = repository.findByApiOperatorCode1AndOperatorName(req.getApiOperatorCode1(), req.getOperatorName());
			if (existingServiceConfigOperatorUpdateMaster != null && !existingServiceConfigOperatorUpdateMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Service OperatorUpdate with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			ServiceConfigOperatorUpdateMaster mst = msts.get();
			mst.setOperatorName(req.getOperatorName());
			mst.setApiOperatorCode1(req.getApiOperatorCode1());
			mst.setApiOperatorCode2(req.getApiOperatorCode2());
			mst.setApiOperatorCode3(req.getApiOperatorCode3());
			mst.setApiOperatorCode4(req.getApiOperatorCode4());
			mst.setApiOperatorCode5(req.getApiOperatorCode5());
			mst.setApiOperatorCode6(req.getApiOperatorCode6());
			mst.setApiOperatorCode7(req.getApiOperatorCode7());
			mst.setApiOperatorCode8(req.getApiOperatorCode8());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service OperatorUpdate modified successfully.");
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
