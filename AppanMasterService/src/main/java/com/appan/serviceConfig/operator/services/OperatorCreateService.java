package com.appan.serviceConfig.operator.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigOperatorMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operator.model.OperatorCreateRequest;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class OperatorCreateService {

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

	private static final Logger logger = LoggerFactory.getLogger(OperatorCreateService.class);

	public CommonResponse create(@Valid OperatorCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			ServiceConfigOperatorMaster existingWallet = repository.findByServiceNameAndOperatorNameIgnoreCase(
					req.getServiceName().trim(), req.getOperatorName().trim());
			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Service Operator with the same name and code already exists.");
				response.setRespCode("01");
				return response;
			}

			Long newId = repository.findMaxId().orElse(0L) + 1;

			String filePath = null;
			if (req.getFile() != null && !req.getFile().isEmpty()) {
				response = myUtils.saveImageToDisk(req.getFile(), "file.png",
						serverDocPath + "serviceconfig/operator/" + newId);
				if (!response.isStatus()) {
					return response;
				}
				filePath = doccumentsUrl + "?docPath=serviceconfig/operator?id=" + newId + "&fileName=file.png";
			}

			ServiceConfigOperatorMaster mst = new ServiceConfigOperatorMaster();
			mst.setOperatorName(req.getOperatorName());
			mst.setServiceName(req.getServiceName());
			mst.setAllowType(req.getAllowType());
			mst.setAmountPlan(req.getAmountPlan());
			mst.setApiCode(req.getApiCode());
			mst.setFile(filePath);
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
			mst.setCreatedBy(req.getUsername().toUpperCase());
			mst.setCreatedDt(new Date());
			mst.setAuthStatus("4");
			mst.setMinAmount(req.getMinAmount());
			mst.setMaxAmount(req.getMaxAmount());
			mst.setIsBbps(req.getIsBbps());
			mst.setIsValidate(req.getIsValidate());
			mst.setIsPartial(req.getIsPartial());
			mst.setIsRandom(req.getIsRandom());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service Operator created successfully.");
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
