package com.appan.ticketmanagement.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TicketManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TicketManagementMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.ticketmanagement.model.TicketUpdateToRequest;

@Service
public class TicketModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TicketManagementMasterRepository ticketManagementMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(TicketModifyService.class);

	public CommonResponse update(TicketUpdateToRequest req) {
		Logger.info("\r\n\r\n**************************** UPDATE TICKET *************************************");
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			TicketManagementMaster ticket = ticketManagementMasterRepository.findById(req.getId()).orElse(null);
			if (ticket == null) {
				response.setStatus(false);
				response.setMessage("Ticket with given ID not found");
				response.setRespCode("01");
				return response;
			}

			ticket.setRemark(req.getRemark());
			ticket.setModifyBy(req.getUsername().toUpperCase());
			ticket.setModifyDt(new Date());

			ticketManagementMasterRepository.save(ticket);

			response.setStatus(true);
			response.setMessage("Ticket updated successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

	public CommonResponse assignto(TicketAssignToRequest req) {
		Logger.info("\r\n\r\n**************************** ASSIGN TO TICKET *************************************");
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			TicketManagementMaster newTicket = new TicketManagementMaster();
			newTicket.setAssignedTo(req.getAssignedTo());
			newTicket.setAssignedDate(new Date());
			newTicket.setAssignedBy(req.getUsername().toUpperCase());

			ticketManagementMasterRepository.save(newTicket);

			response.setStatus(true);
			response.setMessage("Ticket assign to successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
