package com.appan.businessmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.BusinessCategory.model.BusinessCategoryCreateRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BusinessCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class BusinessCategoryCreateService {

	@Value("${documents_url}")
	private String documentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private BusinessCategoryRepository businessCategoryRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessCategoryCreateService.class);

	public CommonResponse createBusinessCategory(@Valid BusinessCategoryCreateRequest req) {

		logger.info(
				"\r\n\r\n**************************** CREATE BUSINESS ENTITY TYPE *************************************");

		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			String categoryCode = req.getCategoryCode().toLowerCase();

			boolean categoryExists = businessCategoryRepository.existsByCategoryCodeIgnoreCase(categoryCode);

			if (categoryExists) {
				response.setStatus(false);
				response.setMessage("Business Category with this code already exists.");
				response.setRespCode("02");
				return response;
			}

			BusinessCategory ms = businessCategoryRepository.findTopByOrderByIdDesc();

			Long newId = ms.getId() + 1;

			response = myUtils.saveImageToDisk(req.getImage(), "image.png",
					serverDocPath + "business/businesscategory/" + newId);
			if (!response.isStatus()) {
				return response;
			}

			String imgPath = documentsUrl + "?docPath=business/businesscategory?id=" + newId + "&fileName=image.png";

			BusinessCategory businessCategory = new BusinessCategory();
			businessCategory.setCategoryName(req.getCategoryName());
			businessCategory.setCategoryCode(req.getCategoryCode());
			businessCategory.setImage(imgPath);
			businessCategory.setStatus(req.getStatus());
			businessCategory.setCreatedBy(req.getUsername().toUpperCase());
			businessCategory.setCreatedDt(new Date());
			businessCategory.setAuthStatus("4");

			businessCategoryRepository.save(businessCategory);

			response.setStatus(true);
			response.setMessage("Business Category created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
		return response;
	}

}
