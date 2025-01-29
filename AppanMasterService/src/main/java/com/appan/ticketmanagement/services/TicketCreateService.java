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
import com.appan.ticketmanagement.model.TicketCreateRequest;

@Service
public class TicketCreateService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private TicketManagementMasterRepository ticketManagementMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(TicketCreateService.class);

	public CommonResponse create(TicketCreateRequest req) {
		CommonResponse response = new CommonResponse();
		Logger.info("\r\n\r\n**************************** CREATE TICKET *************************************");
		try {
			// Check if user exists
			UserMaster user = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Optionally check if a ticket with the same subject or other criteria exists
			// Example: check if a ticket with the same subject exists
			TicketManagementMaster existingTicket = ticketManagementMasterRepository.findBySubject(req.getSubject());
			if (existingTicket != null) {
				response.setStatus(false);
				response.setMessage("Ticket with the same subject already exists");
				response.setRespCode("01");
				return response;
			}

			// Create and populate a new ticket
			TicketManagementMaster ticket = new TicketManagementMaster();
			ticket.setUserName(req.getUserName());
			ticket.setType(req.getType());
			ticket.setSubject(req.getSubject());
			ticket.setTicketMessage(req.getTicketMessage());
			ticket.setPriority(req.getPriority());
			ticket.setApi(req.getApi());
			ticket.setComplainProof(req.getComplainProof());
			ticket.setRequestedDate(req.getRequestedDate());
			ticket.setAssignedDate(req.getAssignedDate());
			ticket.setAssignedTo(req.getAssignedTo());
			ticket.setAssignedBy(req.getAssignedBy());
			ticket.setStatus(req.getStatus());
			ticket.setApiStatus(req.getApiStatus());
			ticket.setTransactionId(req.getTransactionId());
			ticket.setRaiseToApi(req.getRaiseToApi());
			ticket.setLogs(req.getLogs());
			ticket.setRemark(req.getRemark());
			ticket.setClosedBy(req.getClosedBy());
			ticket.setClosedDate(req.getClosedDate());
			ticket.setCreatedBy(req.getUsername().toUpperCase());
			ticket.setCreatedDt(new Date());

			// Save the new ticket to the repository
			ticketManagementMasterRepository.save(ticket);

			// Prepare success response
			response.setStatus(true);
			response.setMessage("Ticket created successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			Logger.error("Error while creating ticket: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred while creating ticket");
			response.setRespCode("03");
			return response;
		}
	}
}
