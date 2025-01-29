package com.appan.registration.fields.services;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.RegistrationFields;
import com.appan.entity.UserMaster;
import com.appan.registration.fields.model.FieldsModifyRequest;
import com.appan.repositories.Repositories.FieldsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class FieldsModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private FieldsMasterRepository repository;

	public CommonResponse modify(FieldsModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<RegistrationFields> mst = repository.findById(req.getId());
			if (!mst.isPresent()) {
				response.setStatus(false);
				response.setMessage("Registration Fields with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			RegistrationFields existingRegistrationFields = repository.findByfieldsNameAndFieldsType(req.getStepsName(), req.getLabel());
			if (existingRegistrationFields != null && !existingRegistrationFields.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Registration Fields with the given name or Type already exists");
				response.setRespCode("02");
				return response;
			}

			RegistrationFields ms = mst.get();
			ms.setFieldsName(req.getFieldsName());
			ms.setFieldsType(req.getFieldsType());
			ms.setStepsName(req.getStepsName());
			ms.setLabel(req.getLabel());
			ms.setUserType(req.getUserType());
			ms.setEntityType(req.getEntityType());
			ms.setRank(req.getRank());
			ms.setWebRegex(req.getWebRegex());
			ms.setMobileRegex(req.getMobileRegex());
			ms.setIsMandatory(req.getIsMandatory());
			ms.setIsSignup(req.getIsSignup());
			ms.setIsGroup(req.getIsGroup());
			ms.setHasCaps(req.getHasCaps());
			ms.setIsVerified(req.getIsVerified());
			ms.setIsDocumentGroup(req.getIsDocumentGroup());
			ms.setStatus(req.getStatus());
			ms.setValidationMessage(req.getValidationMessage());
			ms.setGroupType(req.getGroupType());
			ms.setDocumentGroup(req.getDocumentGroup());
			ms.setMinLength(req.getMinLength());
			ms.setMaxLength(req.getMaxLength());
			ms.setModifyBy(req.getUsername().toUpperCase());
			ms.setModifyDt(new Date());
			repository.save(ms);

			response.setStatus(true);
			response.setMessage("Registration Fields modified successfully.");
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
