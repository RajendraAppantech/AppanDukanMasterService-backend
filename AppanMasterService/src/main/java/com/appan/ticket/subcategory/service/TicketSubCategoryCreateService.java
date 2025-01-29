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
import com.appan.ticket.subcategory.models.TicketSubCategoryCreateRequest;
import com.appan.utils.MyUtils;

@Service
public class TicketSubCategoryCreateService {

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

	public CommonResponse create(TicketSubCategoryCreateRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			TicketSubCategory master = repository.findBySubCategoryName(req.getSubCategoryName());
			if (master != null) {
				response.setStatus(false);
				response.setMessage("Ticket sub category already exist");
				response.setRespCode("01");
				return response;
			}

            Long newId = repository.findMaxTicketSubCategoryId().orElse(0L) + 1;

            response = myUtils.saveImageToDisk(req.getIcon(), "icon.png", serverDocPath + "ticket/subcategory/" + newId);
            if (!response.isStatus()) {
                return response;
            }

            String imgPath = doccumentsUrl + "?docPath=ticket/subcategory?id=" + newId + "&fileName=icon.png";


			TicketSubCategory mst = new TicketSubCategory();
			mst.setCategory(req.getCategory());
			mst.setSubCategoryName(req.getSubCategoryName());
			mst.setIcon(imgPath);
			mst.setPriority(req.getPriority());
			mst.setStatus(req.getStatus());
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Ticket sub category created successfully.");
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
