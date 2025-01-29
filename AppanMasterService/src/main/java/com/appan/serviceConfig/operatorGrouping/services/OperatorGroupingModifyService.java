package com.appan.serviceConfig.operatorGrouping.services;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigOperatorGroupingMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorGroupingRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operatorGrouping.model.OperatorGroupingModifyRequest;

@Service
public class OperatorGroupingModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorGroupingRepository repository;

	public CommonResponse modify(OperatorGroupingModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<ServiceConfigOperatorGroupingMaster> msts = repository.findById(req.getId());
			if (!msts.isPresent()) {
				response.setStatus(false);
				response.setMessage("Service OperatorGrouping with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorGroupingMaster existingServiceConfigOperatorGroupingMaster = repository.findByGroupNameAndOperatorNameAndParameterName(req.getGroupName().trim() , req.getOperatorName().trim(), req.getParameterName().trim());
			if (existingServiceConfigOperatorGroupingMaster != null && !existingServiceConfigOperatorGroupingMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Service OperatorGrouping with the same Operator And Paramete name already exists.");
				response.setRespCode("02");
				return response;
			}

			ServiceConfigOperatorGroupingMaster mst = msts.get();
			mst.setGroupName(req.getGroupName());
			mst.setOperatorName(req.getOperatorName());
			mst.setParameterName(req.getParameterName());
			mst.setValue(req.getValue());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service OperatorGrouping modified successfully.");
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
