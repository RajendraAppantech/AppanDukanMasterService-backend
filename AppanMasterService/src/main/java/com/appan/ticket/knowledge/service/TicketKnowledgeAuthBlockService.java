package com.appan.ticket.knowledge.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TicketKnowledge;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TicketKnowledgeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.ticket.knowledge.models.TicketKnowledgeAuthOrBlockRequest;
import com.google.gson.GsonBuilder;

@Service
public class TicketKnowledgeAuthBlockService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	private static final Logger Logger = LoggerFactory.getLogger(TicketKnowledgeAuthBlockService.class);

	@Autowired
	private TicketKnowledgeRepository repository;
	
	@Autowired
	private UserMasterRepository userMasterRepository;

	public CommonResponse authorblock(TicketKnowledgeAuthOrBlockRequest req) {
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

			TicketKnowledge mst = repository.findByTicketKnowledgeId(req.getTicketKnowledgeId());
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Ticket child category id not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && mst.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Ticket knowledge category already authorized.");
				response.setRespCode("01");
				return response;
			}

			mst.setAuthBy(req.getUsername().toUpperCase());
			mst.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				mst.setAuthStatus("1");
				response.setMessage("Ticket Knowledge category authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				mst.setAuthStatus("3");
				response.setMessage("Ticket Knowledge category block successfully.");
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