package com.appan.businessmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.BusinessSubCategory.model.BusinessSubCategoryAuthOrBlockRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BusinessSubCategory;
import com.appan.repositories.Repositories.BusinessSubCategoryRepository;

import jakarta.validation.Valid;

@Service
public class BusinessSubCategoryAuthOrBlockService {

	@Autowired
	private BusinessSubCategoryRepository businessSubCategoryRepository;

//	@Autowired
//	private UserMasterRepository userMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessSubCategoryAuthOrBlockService.class);

	public CommonResponse authorBlockBusinessSubCategory(@Valid BusinessSubCategoryAuthOrBlockRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK Business SubCategory *************************************");
		CommonResponse response = new CommonResponse();

		try {
//			// Step 1: Validate user existence for authorization
//			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
//			if (master == null) {
//				response.setStatus(false);
//				response.setMessage(ErrorMessages.INVALID_USERNAME);
//				response.setRespCode("01");
//				return response;
//			}

			// Step 2: Check if Business SubCategory exists by ID
			BusinessSubCategory businessSubCategory = businessSubCategoryRepository.findById(req.getId()).orElse(null);
			if (businessSubCategory == null) {
				response.setStatus(false);
				response.setMessage("Business subcategory not found for the provided ID.");
				response.setRespCode("02");
				return response;
			}

			// Step 3: Check if the Business SubCategory is already authorized or blocked
			if ("1".equalsIgnoreCase(req.getStatus()) && "1".equalsIgnoreCase(businessSubCategory.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Business subcategory is already authorized.");
				response.setRespCode("03");
				return response;
			}

			if ("3".equalsIgnoreCase(req.getStatus()) && "3".equalsIgnoreCase(businessSubCategory.getAuthStatus())) {
				response.setStatus(false);
				response.setMessage("Business subcategory is already blocked.");
				response.setRespCode("04");
				return response;
			}

			// Step 4: Update the Business SubCategory's authStatus and modify fields
			businessSubCategory.setAuthStatus(req.getStatus());
			businessSubCategory.setAuthBy(req.getUsername().toUpperCase());
			businessSubCategory.setAuthDate(new Date());

			businessSubCategoryRepository.save(businessSubCategory);

			if ("1".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Business subcategory successfully authorized.");
			} else if ("3".equalsIgnoreCase(req.getStatus())) {
				response.setMessage("Business subcategory successfully blocked.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid status.");
				return response;
			}

			response.setStatus(true);
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}
		return response;
	}
}
