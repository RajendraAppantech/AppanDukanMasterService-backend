package com.appan.walletmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SourceOfFund;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.SourceOfFundRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.walletmaster.model.SourceOfFundCreateRequest;

import jakarta.validation.Valid;

@Service
public class SourceOfFundCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SourceOfFundRepository sourceOfFundRepository;

	private static final Logger logger = LoggerFactory.getLogger(SourceOfFundCreateService.class);

	public CommonResponse createSourceOfFund(@Valid SourceOfFundCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			SourceOfFund existingSource = sourceOfFundRepository
					.findBySourceNameAndCodeIgnoreCase(req.getSourceName().trim(), req.getCode().trim());

			if (existingSource != null) {
				response.setStatus(false);
				response.setMessage("Source of fund with the same name and code already exists.");
				response.setRespCode("01");
				return response;
			}

			SourceOfFund newSourceOfFund = new SourceOfFund();
			newSourceOfFund.setSourceName(req.getSourceName());
			newSourceOfFund.setCategoryName(req.getCategoryName());
			newSourceOfFund.setRank(req.getRank());
			newSourceOfFund.setCode(req.getCode());
			newSourceOfFund.setDescription(req.getDescription());
			newSourceOfFund.setStatus(req.getStatus());
			newSourceOfFund.setCreatedBy(req.getUsername().toUpperCase());
			newSourceOfFund.setCreatedDt(new Date());
			newSourceOfFund.setAuthStatus("4");

			sourceOfFundRepository.save(newSourceOfFund);

			response.setStatus(true);
			response.setMessage("Source of fund created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}

		return response;
	}
}
