package com.appan.countrymaster.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.pincode.models.ModifyPincodeRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.PincodeMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.PincodeMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifyPincodeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private PincodeMasteRepository pincodeMasteRepository;

	public CommonResponse modify(@Valid ModifyPincodeRequest req) {

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			Optional<PincodeMaster> existingPincodeOpt = pincodeMasteRepository.findById(req.getPincodeId());
			if (!existingPincodeOpt.isPresent()) {
				response.setStatus(false);
				response.setMessage("Pincode with ID " + req.getPincodeId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			PincodeMaster existingPincode = existingPincodeOpt.get();

			boolean isDuplicate = pincodeMasteRepository
					.existsByPincodeIgnoreCaseAndBlockPoNameIgnoreCaseAndStatusIgnoreCaseAndPincodeIdNot(
							req.getPincode(), req.getBlockPoName(), req.getStatus(), req.getPincodeId());
			if (isDuplicate) {
				response.setStatus(false);
				response.setMessage(
						"Duplicate entry: The combination of Pincode, Block PO name, and Status already exists.");
				response.setRespCode("02");
				return response;
			}

			existingPincode.setPincode(req.getPincode());
			existingPincode.setBlockPoName(req.getBlockPoName());
			existingPincode.setStatus(req.getStatus());
			existingPincode.setModifyBy(req.getUsername().toUpperCase());
			existingPincode.setModifyDt(new java.util.Date());

			pincodeMasteRepository.save(existingPincode);

			response.setStatus(true);
			response.setMessage("Pincode updated successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
