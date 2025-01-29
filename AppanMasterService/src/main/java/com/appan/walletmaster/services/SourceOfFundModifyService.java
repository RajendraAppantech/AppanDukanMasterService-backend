package com.appan.walletmaster.services;

import java.util.Date;
import java.util.Optional;

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
import com.appan.walletmaster.model.SourceOfFundModifyRequest;

@Service
public class SourceOfFundModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SourceOfFundRepository sourceOfFundRepository;

	private static final Logger logger = LoggerFactory.getLogger(SourceOfFundModifyService.class);

	public CommonResponse modifySourceOfFund(SourceOfFundModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<SourceOfFund> sourceOfFundOptional = sourceOfFundRepository.findById(req.getId());
			if (!sourceOfFundOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("Source of Fund with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			SourceOfFund existingSourceOfFund = sourceOfFundRepository
					.findBySourceNameAndCategoryName(req.getSourceName(), req.getCategoryName());

			if (existingSourceOfFund != null && !existingSourceOfFund.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Source of Fund with the given name and category already exists");
				response.setRespCode("02");
				return response;
			}

			SourceOfFund sourceOfFund = sourceOfFundOptional.get();
			sourceOfFund.setSourceName(req.getSourceName());
			sourceOfFund.setCategoryName(req.getCategoryName());
			sourceOfFund.setRank(req.getRank());
			sourceOfFund.setCode(req.getCode());
			sourceOfFund.setDescription(req.getDescription());
			sourceOfFund.setStatus(req.getStatus());
			sourceOfFund.setModifyBy(req.getUsername().toUpperCase());
			sourceOfFund.setModifyDt(new Date());

			// Save the modified data
			sourceOfFundRepository.save(sourceOfFund);

			// Prepare response
			response.setStatus(true);
			response.setMessage("Source of Fund modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Error occurred while modifying source of fund: ", e);

			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
