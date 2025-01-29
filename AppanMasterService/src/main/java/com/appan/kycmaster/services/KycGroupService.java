package com.appan.kycmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.kyc.kycgroup.model.CreateKycGroupRequest;
import com.appan.kycmaster.entity.KycGroupMaster;
import com.appan.kycmaster.repositories.KycRepositories.KycGroupMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class KycGroupService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private KycGroupMasterRepository kycGroupMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(KycGroupService.class);

	public CommonResponse create(@Valid CreateKycGroupRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			String groupCode = req.getCode().toLowerCase();

			boolean groupExists = kycGroupMasterRepository.existsByCodeIgnoreCase(groupCode);

			if (groupExists) {
				response.setStatus(false);
				response.setMessage("KYC Group with this code already exists.");
				response.setRespCode("02");
				return response;
			}

			KycGroupMaster kycGroup = new KycGroupMaster();
			kycGroup.setKycGroupName(req.getKycGroupName());
			kycGroup.setDescription(req.getDescription());
			kycGroup.setCode(req.getCode());
			kycGroup.setPriority(req.getPriority());
			kycGroup.setStatus(req.getStatus());
			kycGroup.setCreatedBy(req.getUsername().toUpperCase());
			kycGroup.setCreatedDt(new Date());
			kycGroup.setAuthStatus("4");

			kycGroupMasterRepository.save(kycGroup);

			response.setStatus(true);
			response.setMessage("KYC Group created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
