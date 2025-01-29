package com.appan.countrymaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.AuthOrBlockRegionRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.RegionMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.RegionMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.gson.GsonBuilder;

@Service
public class AuthBlockRegionService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(AuthBlockRegionService.class);

	@Autowired
	private RegionMasteRepository repository;

	public CommonResponse authorblock(AuthOrBlockRegionRequest req) {
		Logger.info("\r\n\r\n**************************** AUTH OR BLOCK REGION *************************************");
		CommonResponse response = new CommonResponse();
		try {

			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			RegionMaster mst = repository.findByRegionId(req.getRegionId());
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Region id not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && mst.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Region already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && mst.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Region is already blocked.");
				response.setRespCode("01");
				return response;
			}

			mst.setAuthBy(req.getUsername().toUpperCase());
			mst.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				mst.setAuthStatus("1");
				response.setMessage("Region authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				mst.setAuthStatus("3");
				response.setMessage("Region block successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			repository.save(mst);
			response.setStatus(true);
			response.setRespCode("00");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}