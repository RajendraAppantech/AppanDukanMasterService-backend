package com.appan.serviceConfig.category.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigCategoryMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.category.model.CategoryCreateRequest;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class CategoryCreateService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigCategoryRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(CategoryCreateService.class);

	public CommonResponse create(@Valid CategoryCreateRequest req) {
	    CommonResponse response = new CommonResponse();

	    try {
	        UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
	        if (user == null) {
	            response.setStatus(false);
	            response.setMessage(ErrorMessages.INVALID_USERNAME);
	            response.setRespCode("01");
	            return response;
	        }

	        ServiceConfigCategoryMaster existingWallet = repository.findByCategoryNameAndCategoryCodeIgnoreCase(
	                req.getCategoryName().trim(), req.getCategoryCode().trim());
	        if (existingWallet != null) {
	            response.setStatus(false);
	            response.setMessage("Service Category with the same name and code already exists.");
	            response.setRespCode("01");
	            return response;
	        }

	        Long newId = repository.findMaxId().orElse(0L) + 1;

	        
	        if (req.getImage() != null && !req.getImage().isEmpty()) {
	            
	            response = myUtils.saveImageToDisk(req.getImage(), "image.png", serverDocPath + "serviceconfig/category/" + newId);
	            if (!response.isStatus()) {
	                return response;
	            }
	        }

	        
	        String imgPath = (req.getImage() != null && !req.getImage().isEmpty()) ?
	                doccumentsUrl + "?docPath=serviceconfig/category?id=" + newId + "&fileName=image.png" : null;

	        ServiceConfigCategoryMaster mst = new ServiceConfigCategoryMaster();
	        mst.setCategoryName(req.getCategoryName());
	        mst.setCategoryCode(req.getCategoryCode());
	        mst.setIsTpin(req.getIsTpin());
	        mst.setImage(imgPath); 
	        mst.setStatus(req.getStatus());
	        mst.setCreatedBy(req.getUsername().toUpperCase());
	        mst.setCreatedDt(new Date());
	        mst.setAuthStatus("4");
	        repository.save(mst);

	        response.setStatus(true);
	        response.setMessage("Service Category created successfully.");
	        response.setRespCode("00");
	    } catch (Exception e) {
	        logger.error("Exception occurred while creating wallet: ", e);
	        response.setStatus(false);
	        response.setMessage("EXCEPTION");
	        response.setRespCode("EX");
	    }

	    return response;
	}

}
