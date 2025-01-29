package com.appan.notification.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SmsTemplateMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.SmsTemplateModifyRequest;
import com.appan.repositories.Repositories.SmsTemplateMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class SmsTemplateModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SmsTemplateMasterRepository repository;

	public CommonResponse modify(SmsTemplateModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<SmsTemplateMaster> msts = repository.findById(req.getId());
			if (!msts.isPresent()) {
				response.setStatus(false);
				response.setMessage("Sms Template with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			SmsTemplateMaster existingSmsTemplateMaster = repository.findBySmsTypeAndTemplateId(req.getSmsType(),
					req.getTemplateId());
			if (existingSmsTemplateMaster != null && !existingSmsTemplateMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Sms Template with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			SmsTemplateMaster mst = msts.get();
			mst.setSmsContent(req.getSmsContent());
			mst.setSmsType(req.getSmsType());
			mst.setTemplateId(req.getTemplateId());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Sms Template modified successfully.");
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
