package com.appan.ticket.knowledge.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TicketKnowledge;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TicketKnowledgeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.ticket.knowledge.models.TicketKnowledgeCreateRequest;
import com.appan.utils.MyUtils;

@Service
public class TicketKnowledgeCreateService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TicketKnowledgeRepository repository;

	public CommonResponse create(TicketKnowledgeCreateRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			TicketKnowledge master = repository.findByKnowledgeCategoryName(req.getKnowledgeCategoryName());
			if (master != null) {
				response.setStatus(false);
				response.setMessage("Ticket Knowledge category already exist");
				response.setRespCode("01");
				return response;
			}

			Long newId = repository.findMaxTicketKnowledgeId().orElse(0L) + 1;
			response = myUtils.saveImageToDisk(req.getIcon(), "icon.png",
					serverDocPath + "ticket/knowledgecategory/" + newId);
			if (!response.isStatus()) {
				return response;
			}

			String imgPath = doccumentsUrl + "?docPath=ticket/knowledgecategory?id=" + newId + "&fileName=icon.png";

			TicketKnowledge mst = new TicketKnowledge();
			mst.setKnowledgeCategoryName(req.getKnowledgeCategoryName());
			mst.setChildCategory(req.getChildCategory());
			mst.setDescription(req.getDescription());
			mst.setIcon(imgPath);
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Ticket Knowledge category created successfully.");
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
