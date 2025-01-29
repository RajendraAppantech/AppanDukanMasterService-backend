package com.appan.ticket.subcategory.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TicketSubCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TicketSubCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.ticket.subcategory.models.TicketSubCategoryModifyRequest;
import com.appan.utils.MyUtils;

@Service
public class TicketSubCategoryModifyService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TicketSubCategoryRepository repository;

	public CommonResponse modify(TicketSubCategoryModifyRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			TicketSubCategory mst = repository.findByTicketSubCategoryId(req.getTicketSubCategoryId());
			if (mst == null) {
				response.setStatus(false);
				response.setMessage("Ticket sub Category id not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getIcon() != null && !req.getIcon().isEmpty()) {
				if (!req.getIcon().startsWith("http") && !req.getIcon().startsWith("https")) {
					response = myUtils.saveImageToDisk(req.getIcon(), "icon.png",
							serverDocPath + "ticket/subcategory/" + mst.getTicketSubCategoryId());
					if (!response.isStatus()) {
						return response;
					}

					String imgPath = doccumentsUrl + "?docPath=ticket/subcategory?id=" + mst.getTicketSubCategoryId()
							+ "&fileName=image.png";
					mst.setIcon(imgPath);
				}
			}

			mst.setSubCategoryName(req.getSubCategoryName());
			mst.setCategory(req.getCategory());
			mst.setPriority(req.getPriority());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Ticket sub Category modify successfully.");
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