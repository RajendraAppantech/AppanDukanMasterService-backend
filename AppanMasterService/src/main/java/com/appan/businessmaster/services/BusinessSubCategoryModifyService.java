package com.appan.businessmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.BusinessSubCategory.model.BusinessSubCategoryModifyRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BusinessSubCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessSubCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class BusinessSubCategoryModifyService {

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

	private static final Logger logger = LoggerFactory.getLogger(BusinessSubCategoryModifyService.class);

	public CommonResponse modifyBusinessSubCategory(@Valid BusinessSubCategoryModifyRequest req) {
		logger.info(
				"\r\n\r\n**************************** MODIFY BUSINESS SUBCATEGORY *************************************");
		CommonResponse response = new CommonResponse();

		try {
			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			BusinessSubCategory existingSubCategory = businessSubCategoryRepository.findById(req.getId()).orElse(null);
			if (existingSubCategory == null) {
				response.setStatus(false);
				response.setMessage("Business subcategory entry not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}
			BusinessSubCategory duplicateSubCategory = businessSubCategoryRepository
					.findBySubCategoryNameOrCategoryCodeIgnoreCase(req.getSubCategoryName(), req.getCategoryCode());

			if (duplicateSubCategory != null && !duplicateSubCategory.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage(
						"Another entry with the same subcategory name or category code already exists (case-insensitive).");
				response.setRespCode("03");
				return response;
			}

			if (req.getImage() != null && !req.getImage().isEmpty()) {
				if (!req.getImage().startsWith("http") && !req.getImage().startsWith("https")) {
				
				response = myUtils.saveImageToDisk(req.getImage(), "image.png",
						serverDocPath + "business/businesssubcategory/" + req.getId());
				if (!response.isStatus()) {
					return response;
				}

				String filePath = documentsUrl + "?docPath=business/businesssubcategory?id=" + req.getId() + "&fileName=image.png";
				existingSubCategory.setImage(filePath);
			}
				
			}

			existingSubCategory.setSubCategoryName(req.getSubCategoryName());
			existingSubCategory.setCategoryName(req.getCategoryName());
			existingSubCategory.setCategoryCode(req.getCategoryCode());
			existingSubCategory.setStatus(req.getStatus());
			existingSubCategory.setModifyBy(req.getUsername().toUpperCase());
			existingSubCategory.setModifyDt(new Date());

			businessSubCategoryRepository.save(existingSubCategory);

			response.setStatus(true);
			response.setMessage("Business subcategory modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
