package com.appan.paymentmaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.PaymentModeMaster;
import com.appan.entity.UserMaster;
import com.appan.paymentmaster.model.PaymentModeCreateRequest;
import com.appan.repositories.Repositories.PaymentModeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class PaymentmodeCreateService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private PaymentModeMasterRepository paymentModeMasterRepository;

	public CommonResponse create(@Valid PaymentModeCreateRequest req) {
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

			// Check if payment mode already exists
			PaymentModeMaster existingPaymentMode = paymentModeMasterRepository.findByPaymentMode(req.getPaymentMode());
			if (existingPaymentMode != null) {
				response.setStatus(false);
				response.setMessage("Payment mode already exists");
				response.setRespCode("01");
				return response;
			}

			PaymentModeMaster ms = paymentModeMasterRepository.findTopByOrderByIdDesc();

			Long newId;
			if (ms != null) {
			    newId = ms.getId() + 1;
			} else {
			    newId = 1L; 
			}

			response = myUtils.saveImageToDisk(req.getImage(), "image.png",
					serverDocPath + "payment/paymentmode/" + newId);
			if (!response.isStatus()) {
				return response;
			}

			String imgPath = doccumentsUrl + "?docPath=payment/paymentmode?id=" + newId + "&fileName=image.png";

			// Create a new payment mode entry
			PaymentModeMaster newPaymentMode = new PaymentModeMaster();
			newPaymentMode.setPaymentMode(req.getPaymentMode());
			newPaymentMode.setImage(imgPath);
			newPaymentMode.setStatus(req.getStatus());
			newPaymentMode.setCreatedBy(req.getUsername().toUpperCase());
			newPaymentMode.setCreatedDt(new Date());
			newPaymentMode.setAuthStatus("4");

			paymentModeMasterRepository.save(newPaymentMode);

			response.setStatus(true);
			response.setMessage("Payment mode created successfully.");
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
