package com.appan.countrymaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.district.models.AuthOrBlockDistrictRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.DistrictMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.DistrictMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.gson.GsonBuilder;

import jakarta.validation.Valid;

@Service
public class AuthBlockDistrictService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DistrictMasteRepository districtMasteRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(AuthBlockDistrictService.class);

	public CommonResponse authorblock(@Valid AuthOrBlockDistrictRequest req) {
		Logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK DISTRICT *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			DistrictMaster mst = districtMasteRepository.findByDistrictId(req.getDistrictId());
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("District ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && "1".equalsIgnoreCase(mst.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("District is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && "3".equalsIgnoreCase(mst.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("District is already blocked.");
				response.setRespCode("01");
				return response;
			}

			mst.setAuthBy(req.getUsername().toUpperCase());
			mst.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				mst.setAuthStatus("1");
				response.setMessage("District authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				mst.setAuthStatus("3");
				response.setMessage("District blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			districtMasteRepository.save(mst);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
