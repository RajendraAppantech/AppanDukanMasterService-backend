package com.appan.countrymaster.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.pincode.models.CreateStateCityBlockRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BlockPoMaster;
import com.appan.entity.CityMaster;
import com.appan.entity.PincodeMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BlockPoMasteRepository;
import com.appan.repositories.Repositories.CityMasteRepository;
import com.appan.repositories.Repositories.PincodeMasteRepository;
import com.appan.repositories.Repositories.StateMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class FetchStateCityBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private PincodeMasteRepository pincodeMasteRepository;

	@Autowired
	private StateMasteRepository stateMasteRepository;

	@Autowired
	private CityMasteRepository cityMasteRepository;

	@Autowired
	private BlockPoMasteRepository blockPoMasteRepository;

	public CommonResponse create(@Valid CreateStateCityBlockRequest req) {
		CommonResponse response = new CommonResponse();

		UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
		if (user == null) {
			response.setStatus(false);
			response.setMessage(ErrorMessages.INVALID_USERNAME);
			response.setRespCode("01");
			return response;
		}

		// Check if the pincode exists in the pincode_master table
		PincodeMaster pincodeMaster = pincodeMasteRepository.findByPincode(req.getPincode());
		if (pincodeMaster != null) {
			// Pincode exists, fetch the data
			response.setStatus(true);
			response.setMessage("Pincode found");
			response.setRespCode("00");
			response.setData("pincode", pincodeMaster.getPincode());
			response.setData("block_po_name", pincodeMaster.getBlockPoName());

			// Check if the block_po_name exists in the block_po_master table
			BlockPoMaster blockPoMaster = blockPoMasteRepository.findByBlockPoName(pincodeMaster.getBlockPoName());
			if (blockPoMaster != null) {
				response.setData("city_name", blockPoMaster.getCityName());

				// Fetch district_name and state_name from city_master table
				CityMaster cityMaster = cityMasteRepository.findByCityName(blockPoMaster.getCityName());
				if (cityMaster != null) {
					response.setData("district_name", cityMaster.getDistrictName());
					response.setData("state_name", cityMaster.getStateName());
				} else {
					response.setMessage("City name found, but district_name and state_name not found");
					response.setRespCode("01");
				}
			} else {
				response.setMessage("Pincode and block_po_name found, but city_name not found");
				response.setRespCode("01");
			}
		} else {
			response.setStatus(false);
			response.setMessage("Pincode not found");
			response.setRespCode("01");
		}

		return response;
	}
}
