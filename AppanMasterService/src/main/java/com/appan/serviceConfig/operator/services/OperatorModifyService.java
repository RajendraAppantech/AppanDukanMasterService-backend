package com.appan.serviceConfig.operator.services;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigOperatorMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operator.model.OperatorModifyRequest;
import com.appan.utils.MyUtils;

@Service
public class OperatorModifyService {
	
	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorRepository repository;

	public CommonResponse modify(OperatorModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<ServiceConfigOperatorMaster> msts = repository.findById(req.getId());
			if (!msts.isPresent()) {
				response.setStatus(false);
				response.setMessage("Service Operator with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorMaster existingServiceConfigOperatorMaster = repository.findByServiceNameAndOperatorName(req.getServiceName(),  req.getOperatorName());
			if (existingServiceConfigOperatorMaster != null && !existingServiceConfigOperatorMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Service Operator with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			ServiceConfigOperatorMaster mst = msts.get();
			
			
			 if (req.getFile() != null && !req.getFile().isEmpty()) {
	                if (!req.getFile().startsWith("http") && !req.getFile().startsWith("https")) {
	                    response = myUtils.saveImageToDisk(req.getFile(), "file.png", serverDocPath + "serviceconfig/operator/" + mst.getId());
	                    if (!response.isStatus()) {
	                        return response;
	                    }

	                    String filePath = doccumentsUrl + "?docPath=serviceconfig/operator?id=" + mst.getId() + "&fileName=file.png";
	                    mst.setFile(filePath);
	                }
	            }
			
			mst.setOperatorName(req.getOperatorName());
			mst.setServiceName(req.getServiceName());
			mst.setAllowType(req.getAllowType());
			mst.setAmountPlan(req.getAmountPlan());
			mst.setApiCode(req.getApiCode());
			mst.setTaxType(req.getTaxType());
			mst.setRejectedAmount(req.getRejectedAmount());
			mst.setMinLength(req.getMinLength());
			mst.setMaxLength(req.getMaxLength());
			mst.setMinCommPer(req.getMinCommPer());
			mst.setMaxCommPer(req.getMaxCommPer());
			mst.setMinCommVal(req.getMinCommVal());
			mst.setMaxCommVal(req.getMaxCommVal());
			mst.setMinChargePer(req.getMinChargePer());
			mst.setMaxChargePer(req.getMaxChargePer());
			mst.setMinChargeVal(req.getMinChargeVal());
			mst.setMaxChargeVal(req.getMaxChargeVal());
			mst.setState(req.getState());
			mst.setWalletName(req.getWalletName());
			mst.setChannels(req.getChannels());
			mst.setPaymentMode(req.getPaymentMode());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			mst.setMinAmount(req.getMinAmount());
			mst.setMaxAmount(req.getMaxAmount());
			mst.setIsBbps(req.getIsBbps());
			mst.setIsValidate(req.getIsValidate());
			mst.setIsPartial(req.getIsPartial());
			mst.setIsRandom(req.getIsRandom());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service Operator modified successfully.");
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
