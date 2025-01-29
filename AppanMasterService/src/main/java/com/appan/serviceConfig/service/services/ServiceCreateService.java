package com.appan.serviceConfig.service.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigServiceMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigServiceRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.service.model.ServiceCreateRequest;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class ServiceCreateService {
	
	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigServiceRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(ServiceCreateService.class);

	public CommonResponse create(@Valid ServiceCreateRequest req) {
	    CommonResponse response = new CommonResponse();

	    try {
	        UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
	        if (user == null) {
	            response.setStatus(false);
	            response.setMessage(ErrorMessages.INVALID_USERNAME);
	            response.setRespCode("01");
	            return response;
	        }

	        ServiceConfigServiceMaster existingWallet = repository.findByServiceNameAndServiceCodeIgnoreCase(req.getServiceName().trim(), req.getServiceCode().trim());
	        if (existingWallet != null) {
	            response.setStatus(false);
	            response.setMessage("Service with the same name and code already exists.");
	            response.setRespCode("01");
	            return response;
	        }
	        
	        Long newId = repository.findMaxId().orElse(0L) + 1;

	        // Check if file is provided and not empty
	        String filePath = null;
	        if (req.getFile() != null && !req.getFile().isEmpty()) {
	            // If file is provided, save it to disk
	            response = myUtils.saveImageToDisk(req.getFile(), "file.png", serverDocPath + "serviceconfig/service/" + newId);
	            if (!response.isStatus()) {
	                return response;
	            }
	            filePath = doccumentsUrl + "?docPath=serviceconfig/service?id=" + newId + "&fileName=file.png";
	        }

	        ServiceConfigServiceMaster mst = new ServiceConfigServiceMaster();
	        mst.setServiceName(req.getServiceName());
	        mst.setServiceType(req.getServiceType());
	        mst.setCategoryName(req.getCategoryName());
	        mst.setServiceCode(req.getServiceCode());
	        mst.setIsService(req.getIsService());
	        mst.setIsBbps(req.getIsBbps());
	        mst.setIsBbpsOff(req.getIsBbpsOff());
	        mst.setIsOperatorWise(req.getIsOperatorWise());
	        mst.setFile(filePath); // Set file path to null if no file is provided
	        mst.setWallet(req.getWallet());
	        mst.setStatus(req.getStatus());
	        mst.setCreatedBy(req.getUsername().toUpperCase());
	        mst.setCreatedDt(new Date());
	        mst.setAuthStatus("4");
	        repository.save(mst);

	        response.setStatus(true);
	        response.setMessage("Service created successfully.");
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
