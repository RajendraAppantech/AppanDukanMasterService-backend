package com.appan.serviceConfig.operatorParameter.services;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigOperatorParameterMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorParameterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operatorParameter.model.OperatorParameterModifyRequest;

@Service
public class OperatorParameterModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorParameterRepository repository;

	public CommonResponse modify(OperatorParameterModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<ServiceConfigOperatorParameterMaster> msts = repository.findById(req.getId());
			if (!msts.isPresent()) {
				response.setStatus(false);
				response.setMessage("Service OperatorParameter with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorParameterMaster existingServiceConfigOperatorParameterMaster = repository.findByParameterNameAndOperatorName(req.getParameterName(),  req.getOperatorName());
			if (existingServiceConfigOperatorParameterMaster != null && !existingServiceConfigOperatorParameterMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Service OperatorParameter with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			ServiceConfigOperatorParameterMaster mst = msts.get();
			mst.setOperatorName(req.getOperatorName());
			mst.setParameterName(req.getParameterName());
			mst.setMinLength(req.getMinLenght());
			mst.setMaxLength(req.getMaxLenght());
			mst.setFieldType(req.getFieldType());
			mst.setSort(req.getSort());
			mst.setManualSort(req.getManualSort());
			mst.setPattern(req.getPattern());
			mst.setApiName(req.getApiName());
			mst.setIsActive(req.getIsActive());
			mst.setIsMandatory(req.getIsMandatory());
			mst.setHasGrouping(req.getHasGrouping());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service OperatorParameter modified successfully.");
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
