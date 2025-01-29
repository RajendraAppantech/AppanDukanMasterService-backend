package com.appan.bankmaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.entity.MasterBankData;
import com.appan.bankmaster.masterbankdata.model.MasterBankDataModifyRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.MasterBankDataRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class MasterBankDataModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private MasterBankDataRepository masterBankDataRepository;

	public CommonResponse modifyMasterBankData(@Valid MasterBankDataModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<MasterBankData> existingRecord = masterBankDataRepository.findById(req.getId());

			if (!existingRecord.isPresent()) {
				response.setStatus(false);
				response.setMessage("Master Bank Data with the given ID not found.");
				response.setRespCode("01");
				return response;
			}

			MasterBankData existingBank = masterBankDataRepository.findByBankNameAndIfscCode(req.getBankName(),
					req.getIfscCode());

			if (existingBank != null && !existingBank.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Bank data with the same bank name and IFSC code already exists.");
				response.setRespCode("02");
				return response;
			}

			MasterBankData bankData = existingRecord.get();
			bankData.setBankName(req.getBankName());
			bankData.setIfscCode(req.getIfscCode());
			bankData.setBranch(req.getBranch());
			bankData.setAddress(req.getAddress());
			bankData.setContact(req.getContact());
			bankData.setCity(req.getCity());
			bankData.setState(req.getState());
			bankData.setBlock(req.getBlock());
			bankData.setModifyBy(req.getUsername().toUpperCase());
			bankData.setModifyDt(new Date());

			masterBankDataRepository.save(bankData);

			response.setStatus(true);
			response.setMessage("Master Bank Data modified successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage("Exception occurred during the update process.");
			response.setRespCode("03");
		}

		return response;
	}
}
