package com.appan.paymentmaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SubPaymentMode;
import com.appan.entity.UserMaster;
import com.appan.paymentmaster.model.SubPaymentModeCreateRequest;
import com.appan.repositories.Repositories.SubPaymentModeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class CreateSubPaymentmodeService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private SubPaymentModeRepository subPaymentModeRepository;

	public CommonResponse create(@Valid SubPaymentModeCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			// Check if username exists
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Check if sub-payment mode already exists for the given payment mode
			SubPaymentMode existingSubPaymentMode = subPaymentModeRepository
					.findByPaymentModeAndSubPaymentMode(req.getPaymentMode(), req.getSubPaymentMode());
			if (existingSubPaymentMode != null) {
				response.setStatus(false);
				response.setMessage("Sub-payment mode already exists for the given payment mode");
				response.setRespCode("01");
				return response;
			}

			SubPaymentMode ms = subPaymentModeRepository.findTopByOrderByIdDesc();

			Long newId;
			if (ms != null) {
			    newId = ms.getId() + 1;
			} else {
			    newId = 1L; 
			}

			response = myUtils.saveImageToDisk(req.getImage(), "image.png",
					serverDocPath + "payment/subpaymentmode/" + newId);
			if (!response.isStatus()) {
				return response;
			}

			String imgPath = doccumentsUrl + "?docPath=payment/subpaymentmode?id=" + newId + "&fileName=image.png";

			// Create a new sub-payment mode entry
			SubPaymentMode newSubPaymentMode = new SubPaymentMode();
			newSubPaymentMode.setPaymentMode(req.getPaymentMode());
			newSubPaymentMode.setSubPaymentMode(req.getSubPaymentMode());
			newSubPaymentMode.setImage(imgPath);
			newSubPaymentMode.setStatus(req.getStatus());
			newSubPaymentMode.setCreatedBy(req.getUsername().toUpperCase());
			newSubPaymentMode.setCreatedDt(new Date());
			newSubPaymentMode.setAuthStatus("4");

			subPaymentModeRepository.save(newSubPaymentMode);

			response.setStatus(true);
			response.setMessage("Sub-payment mode created successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
