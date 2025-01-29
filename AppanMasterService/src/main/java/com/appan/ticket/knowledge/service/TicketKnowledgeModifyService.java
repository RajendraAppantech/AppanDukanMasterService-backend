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
import com.appan.ticket.knowledge.models.TicketKnowledgeModifyRequest;
import com.appan.utils.MyUtils;

@Service
public class TicketKnowledgeModifyService {

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

	public CommonResponse modify(TicketKnowledgeModifyRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			TicketKnowledge mst = repository.findByTicketKnowledgeId(req.getTicketKnowledgeId());
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Ticket knowledge Category id not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getIcon() != null && !req.getIcon().isEmpty()) {
				if (!req.getIcon().startsWith("http") && !req.getIcon().startsWith("https")) {
					response = myUtils.saveImageToDisk(req.getIcon(), "icon.png",
							serverDocPath + "ticket/knowledgecategory/" + mst.getTicketKnowledgeId());
					if (!response.isStatus()) {
						return response;
					}

					String imgPath = doccumentsUrl + "?docPath=ticket/knowledgecategory?id=" + mst.getTicketKnowledgeId()+ "&fileName=image.png";
					mst.setIcon(imgPath);
				}
			}

			mst.setChildCategory(req.getChildCategory());
			mst.setKnowledgeCategoryName(req.getKnowledgeCategoryName());
			mst.setDescription(req.getDescription());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Ticket knowledge Category modify successfully.");
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