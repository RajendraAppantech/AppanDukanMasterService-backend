package com.appan.notification.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.EmailTemplateMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.EmailTemplateModifyRequest;
import com.appan.repositories.Repositories.EmailTemplateMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class EmailTemplateModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private EmailTemplateMasterRepository repository;

	public CommonResponse modify(EmailTemplateModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<EmailTemplateMaster> msts = repository.findById(req.getId());
			if (!msts.isPresent()) {
				response.setStatus(false);
				response.setMessage("Email Template with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			EmailTemplateMaster existingEmailTemplateMaster = repository.findByEmailTypeAndSubject(req.getEmailType(),
					req.getSubject());
			if (existingEmailTemplateMaster != null && !existingEmailTemplateMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Email Template with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			EmailTemplateMaster mst = msts.get();
			mst.setEmailType(req.getEmailType());
			mst.setSubject(req.getSubject());
			mst.setEmailBody(req.getEmailBody());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Email Template modified successfully.");
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
