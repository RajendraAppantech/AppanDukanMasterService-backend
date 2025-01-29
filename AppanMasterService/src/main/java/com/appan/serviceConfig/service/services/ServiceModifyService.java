package com.appan.serviceConfig.service.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigServiceMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigServiceRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.service.model.ServiceModifyRequest;
import com.appan.utils.MyUtils;

@Service
public class ServiceModifyService {

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

	public CommonResponse modify(ServiceModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<ServiceConfigServiceMaster> msts = repository.findById(req.getId());
			if (!msts.isPresent()) {
				response.setStatus(false);
				response.setMessage("Service with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			ServiceConfigServiceMaster existingServiceConfigServiceMaster = repository.findByServiceNameAndServiceCode(req.getServiceName(), req.getServiceCode());
			if (existingServiceConfigServiceMaster != null && !existingServiceConfigServiceMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Service with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			ServiceConfigServiceMaster mst = msts.get();
			
            if (req.getFile() != null && !req.getFile().isEmpty()) {
                if (!req.getFile().startsWith("http") && !req.getFile().startsWith("https")) {
                    response = myUtils.saveImageToDisk(req.getFile(), "file.png", serverDocPath + "serviceconfig/service/" + mst.getId());
                    if (!response.isStatus()) {
                        return response;
                    }

                    String filePath = doccumentsUrl + "?docPath=serviceconfig/service?id=" + mst.getId() + "&fileName=file.png";
                    mst.setFile(filePath);
                }
            }
			
			mst.setServiceName(req.getServiceName());
			mst.setServiceType(req.getServiceType());
			mst.setCategoryName(req.getCategoryName());
			mst.setServiceCode(req.getServiceCode());
			mst.setIsService(req.getIsService());
			mst.setIsBbps(req.getIsBbps());
			mst.setIsBbpsOff(req.getIsBbpsOff());
			mst.setIsOperatorWise(req.getIsOperatorWise());
			mst.setWallet(req.getWallet());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service modified successfully.");
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
