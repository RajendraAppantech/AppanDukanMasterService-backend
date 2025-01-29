package com.appan.ticketmanagement.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.TicketManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TicketManagementMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.ticketmanagement.model.TicketFetchRequest;
import com.appan.ticketmanagement.model.TicketFetchResponse;
import com.appan.ticketmanagement.model.TicketManagementData;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;

@Service
public class TicketFetchService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private TicketManagementMasterRepository ticketManagementMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(TicketFetchService.class);

	public TicketFetchResponse fetch(@Valid TicketFetchRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("**************************** FETCH TICKET DETAILS ****************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<TicketManagementMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<TicketManagementMaster> ticketPage = ticketManagementMasterRepository.findAll(specification, paging);

			if (ticketPage.isEmpty()) {
				return buildErrorResponse(false, "No ticket records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(ticketPage.getNumber() + 1);
			pageDetails.setTotalRecords(ticketPage.getTotalElements());
			pageDetails.setNoOfPages(ticketPage.getTotalPages());
			pageDetails.setPageSize(ticketPage.getSize());

			List<TicketManagementData> ticketData = ticketPage.stream().map(this::convertToTicketManagementData)
					.collect(Collectors.toList());

			return buildSuccessResponse(ticketData, pageDetails);

		} catch (Exception e) {
			Logger.error("Exception in fetch service: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<TicketManagementMaster> root,
			TicketFetchRequest req) {
		List<Predicate> predicates = new ArrayList<>();

		if (req.getUserName() != null && !req.getUserName().isEmpty() && !req.getUserName().equalsIgnoreCase("all")) {
			predicates.add(cb.like(cb.lower(root.get("userName")), "%" + req.getUserName().toLowerCase() + "%"));
		}

		if (req.getType() != null && !req.getType().isEmpty() && !req.getType().equalsIgnoreCase("all")) {
			predicates.add(cb.equal(cb.lower(root.get("type")), req.getType().toLowerCase()));
		}

		if (req.getSubject() != null && !req.getSubject().isEmpty() && !req.getSubject().equalsIgnoreCase("all")) {
			predicates.add(cb.like(cb.lower(root.get("subject")), "%" + req.getSubject().toLowerCase() + "%"));
		}

		if (req.getTicketMessage() != null && !req.getTicketMessage().isEmpty()
				&& !req.getTicketMessage().equalsIgnoreCase("all")) {
			predicates.add(
					cb.like(cb.lower(root.get("ticketMessage")), "%" + req.getTicketMessage().toLowerCase() + "%"));
		}

		if (req.getPriority() != null && !req.getPriority().isEmpty() && !req.getPriority().equalsIgnoreCase("all")) {
			predicates.add(cb.equal(cb.lower(root.get("priority")), req.getPriority().toLowerCase()));
		}

		if (req.getStatus() != null && !req.getStatus().isEmpty() && !req.getStatus().equalsIgnoreCase("all")) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		if (req.getFromDate() != null && req.getToDate() != null) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();
				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				Logger.error("Date parsing exception: ", e);
			}
		}

		return predicates;
	}

	private TicketManagementData convertToTicketManagementData(TicketManagementMaster ticket) {
		TicketManagementData data = new TicketManagementData();
		data.setId(ticket.getId());
		data.setUserName(ticket.getUserName());
		data.setType(ticket.getType());
		data.setSubject(ticket.getSubject());
		data.setTicketMessage(ticket.getTicketMessage());
		data.setPriority(ticket.getPriority());
		data.setApi(ticket.getApi());
		data.setComplainProof(ticket.getComplainProof());
		data.setRequestedDate(ticket.getRequestedDate());
		data.setAssignedDate(ticket.getAssignedDate());
		data.setAssignedTo(ticket.getAssignedTo());
		data.setAssignedBy(ticket.getAssignedBy());
		data.setStatus(ticket.getStatus());
		data.setApiStatus(ticket.getApiStatus());
		data.setTransactionId(ticket.getTransactionId());
		data.setRaiseToApi(ticket.getRaiseToApi());
		data.setLogs(ticket.getLogs());
		data.setRemark(ticket.getRemark());
		data.setClosedBy(ticket.getClosedBy());
		data.setClosedDate(ticket.getClosedDate());
		data.setCreatedBy(ticket.getCreatedBy());
		data.setCreatedDt(ticket.getCreatedDt());
		data.setModifyBy(ticket.getModifyBy());
		data.setModifyDt(ticket.getModifyDt());
		data.setAuthBy(ticket.getAuthBy());
		data.setAuthDate(ticket.getAuthDate());
		return data;
	}

	private TicketFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<TicketManagementData> data, PageDetails pageDetails) {
		TicketFetchResponse response = new TicketFetchResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	private TicketFetchResponse buildSuccessResponse(List<TicketManagementData> data, PageDetails pageDetails) {
		return buildErrorResponse(true, "SUCCESS", "00", data, pageDetails);
	}
}
