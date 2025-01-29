package com.appan.notification.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.AlertTypeMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.AlertModifyRequest;
import com.appan.repositories.Repositories.AlertTypeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class AlertModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private AlertTypeMasterRepository repository;

	public CommonResponse modify(AlertModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<AlertTypeMaster> msts = repository.findById(req.getId());
			if (!msts.isPresent()) {
				response.setStatus(false);
				response.setMessage("Notification Alert with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			AlertTypeMaster existingAlertTypeMaster = repository.findByAlertNameAndParametersName(req.getAlertName(),
					req.getParametersName());
			if (existingAlertTypeMaster != null && !existingAlertTypeMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Notification Alert with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			AlertTypeMaster mst = msts.get();
			mst.setAlertName(req.getAlertName());
			mst.setParametersName(req.getParametersName());
			mst.setIsEmail(req.getIsEmail());
			mst.setIsSms(req.getIsSms());
			mst.setIsWhatsapp(req.getIsWhatsapp());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Notification Alert modified successfully.");
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
