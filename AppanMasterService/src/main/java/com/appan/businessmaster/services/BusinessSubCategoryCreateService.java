package com.appan.businessmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.BusinessSubCategory.model.BusinessSubCategoryCreateRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BusinessSubCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessSubCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class BusinessSubCategoryCreateService {

	@Value("${documents_url}")
	private String documentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private BusinessSubCategoryRepository businessSubCategoryRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessSubCategoryCreateService.class);

	public CommonResponse createBusinessSubCategory(@Valid BusinessSubCategoryCreateRequest req) {

		logger.info(
				"\r\n\r\n**************************** CREATE BUSINESS SUBCATEGORY *************************************");

		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			String categoryName = req.getCategoryName().toLowerCase();
			String subCategoryName = req.getSubCategoryName().toLowerCase();

			boolean subCategoryExists = businessSubCategoryRepository
					.existsByCategoryNameIgnoreCaseAndSubCategoryNameIgnoreCase(categoryName, subCategoryName);

			if (subCategoryExists) {
				response.setStatus(false);
				response.setMessage("Business SubCategory with this name already exists.");
				response.setRespCode("02");
				return response;
			}

			BusinessSubCategory ms = businessSubCategoryRepository.findTopByOrderByIdDesc();

			Long newId = ms.getId() + 1;

			response = myUtils.saveImageToDisk(req.getImage(), "image.png",
					serverDocPath + "business/businesscategory/" + newId);
			if (!response.isStatus()) {
				return response;
			}

			String imgPath = documentsUrl + "?docPath=business/businesssubcategory?id=" + newId + "&fileName=image.png";

			BusinessSubCategory businessSubCategory = new BusinessSubCategory();
			businessSubCategory.setSubCategoryName(req.getSubCategoryName());
			businessSubCategory.setCategoryName(req.getCategoryName());
			businessSubCategory.setCategoryCode(req.getCategoryCode());
			businessSubCategory.setImage(imgPath);
			businessSubCategory.setStatus(req.getStatus());
			businessSubCategory.setCreatedBy(req.getUsername().toUpperCase());
			businessSubCategory.setCreatedDt(new Date());
			businessSubCategory.setAuthStatus("4");

			businessSubCategoryRepository.save(businessSubCategory);

			response.setStatus(true);
			response.setMessage("Business SubCategory created successfully.");
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
