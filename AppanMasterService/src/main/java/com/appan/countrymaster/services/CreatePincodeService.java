package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.pincode.models.CreatePincodeRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.PincodeMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.PincodeMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class CreatePincodeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private PincodeMasteRepository pincodeMasteRepository;

	public CommonResponse create(@Valid CreatePincodeRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			PincodeMaster existingPincode = pincodeMasteRepository.findByPincode(req.getPincode());
			if (existingPincode != null) {
				response.setStatus(false);
				response.setMessage("Pincode already exists");
				response.setRespCode("01");
				return response;
			}

			PincodeMaster newPincode = new PincodeMaster();
			newPincode.setPincode(req.getPincode());
			newPincode.setBlockPoName(req.getBlockPoName());
			newPincode.setStatus(req.getStatus());
			newPincode.setCreatedBy(req.getUsername().toUpperCase());
			newPincode.setCreatedDt(new Date());
			newPincode.setAuthStatus("4");

			pincodeMasteRepository.save(newPincode);

			response.setStatus(true);
			response.setMessage("Pincode created successfully.");
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
