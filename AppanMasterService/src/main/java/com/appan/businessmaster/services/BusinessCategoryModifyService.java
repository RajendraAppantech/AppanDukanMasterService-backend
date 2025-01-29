package com.appan.businessmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.BusinessCategory.model.BusinessCategoryModifyRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BusinessCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class BusinessCategoryModifyService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private BusinessCategoryRepository businessCategoryRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessCategoryModifyService.class);

	public CommonResponse modifyBusinessCategory(@Valid BusinessCategoryModifyRequest req) {
		logger.info(
				"\r\n\r\n**************************** MODIFY BUSINESS CATEGORY *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			BusinessCategory existingCategory = businessCategoryRepository.findById(req.getId()).orElse(null);
			if (existingCategory == null) {
				response.setStatus(false);
				response.setMessage("Business category entry not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			BusinessCategory duplicateCategory = businessCategoryRepository
					.findByCategoryNameAndCategoryCodeIgnoreCase(req.getCategoryName(), req.getCategoryCode());

			if (duplicateCategory != null && !duplicateCategory.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage(
						"Another entry with the same category name and code already exists (case-insensitive).");
				response.setRespCode("03");
				return response;
			}

			if (req.getImage() != null && !req.getImage().isEmpty()) {
				
				if (!req.getImage().startsWith("http") && !req.getImage().startsWith("https")) {
					
			
				response = myUtils.saveImageToDisk(req.getImage(), "image.png", serverDocPath + "business/businesscategory/" + req.getId());
				
				if (!response.isStatus()) {
					return response;
				}

				String filePath = doccumentsUrl + "?docPath=business/businesscategory?id=" + req.getId() + "&fileName=image.png";
				existingCategory.setImage(filePath);
				}
			}

			existingCategory.setCategoryName(req.getCategoryName());
			existingCategory.setCategoryCode(req.getCategoryCode());
			existingCategory.setStatus(req.getStatus());
			existingCategory.setModifyBy(req.getUsername().toUpperCase());
			existingCategory.setModifyDt(new Date());

			businessCategoryRepository.save(existingCategory);

			response.setStatus(true);
			response.setMessage("Business category modified successfully.");
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
