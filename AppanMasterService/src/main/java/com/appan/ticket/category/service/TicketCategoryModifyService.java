package com.appan.ticket.category.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TicketCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TicketCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.ticket.category.models.TicketCategoryModifyRequest;
import com.appan.utils.MyUtils;

@Service
public class TicketCategoryModifyService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TicketCategoryRepository repository;

	public CommonResponse modify(TicketCategoryModifyRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			TicketCategory mst = repository.findByTicketCategoryId(req.getTicketCategoryId());
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Ticket Category id not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getIcon() != null && !req.getIcon().isEmpty()) {
				if (!req.getIcon().startsWith("http") && !req.getIcon().startsWith("https")) {
					response = myUtils.saveImageToDisk(req.getIcon(), "icon.png", serverDocPath + "ticket/category/" + mst.getTicketCategoryId());
					if (!response.isStatus()) {
						return response;
					}

					String imgPath = doccumentsUrl + "?docPath=ticket/category?id=" + mst.getTicketCategoryId()+ "&fileName=image.png";
					mst.setIcon(imgPath);
				}
			}

			mst.setCategoryName(req.getCategoryName());
			mst.setUserType(req.getUserType());
			mst.setPriority(req.getPriority());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Ticket Category modify successfully.");
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