package com.appan.ticket.category.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TicketCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TicketCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.ticket.category.models.TicketCategoryAuthOrBlockRequest;
import com.google.gson.GsonBuilder;

@Service
public class TicketCategoryAuthBlockService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(TicketCategoryAuthBlockService.class);

	@Autowired
	private TicketCategoryRepository repository;
	

	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse authorblock(TicketCategoryAuthOrBlockRequest req) {
		Logger.info("\r\n\r\n**************************** AUTH OR BLOCK *************************************");
		CommonResponse response = new CommonResponse();
		try {

			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			TicketCategory mst = repository.findByTicketCategoryId(req.getTicketCategoryId());
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Ticket category id not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && mst.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Ticket category already authorized.");
				response.setRespCode("01");
				return response;
			}

			mst.setAuthBy(req.getUsername().toUpperCase());
			mst.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				mst.setAuthStatus("1");
				response.setMessage("Ticket category authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				mst.setAuthStatus("3");
				response.setMessage("Ticket category block successfully.");
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