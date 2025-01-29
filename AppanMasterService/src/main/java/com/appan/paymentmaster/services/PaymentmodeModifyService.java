package com.appan.paymentmaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.PaymentModeMaster;
import com.appan.entity.UserMaster;
import com.appan.paymentmaster.model.PaymentModeModifyRequest;
import com.appan.repositories.Repositories.PaymentModeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class PaymentmodeModifyService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private PaymentModeMasterRepository paymentModeRepository;

	public CommonResponse modify(@Valid PaymentModeModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<PaymentModeMaster> existingPaymentModeOpt = paymentModeRepository.findById(req.getId());
			if (!existingPaymentModeOpt.isPresent()) {
				response.setStatus(false);
				response.setMessage("Payment mode with ID " + req.getId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			PaymentModeMaster existingPaymentMode = existingPaymentModeOpt.get();

			boolean isDuplicate = paymentModeRepository.existsByPaymentModeIgnoreCaseAndIdNot(req.getPaymentMode(),
					req.getId());
			if (isDuplicate) {
				response.setStatus(false);
				response.setMessage("Duplicate entry: The payment mode already exists.");
				response.setRespCode("02");
				return response;
			}

			if (req.getImage() != null && !req.getImage().isEmpty()) {
				if (!req.getImage().startsWith("http") && !req.getImage().startsWith("https")) {
					response = myUtils.saveImageToDisk(req.getImage(), "image.png",
							serverDocPath + "payment/paymentmode/" + existingPaymentMode.getId());
					if (!response.isStatus()) {
						return response;
					}

					String imgPath = doccumentsUrl + "?docPath=payment/paymentmode?id=" + existingPaymentMode.getId()
							+ "&fileName=image.png";
					existingPaymentMode.setImage(imgPath);
				}
			}

			existingPaymentMode.setPaymentMode(req.getPaymentMode());
			existingPaymentMode.setStatus(req.getStatus());
			existingPaymentMode.setModifyBy(req.getUsername().toUpperCase());
			existingPaymentMode.setModifyDt(new Date());

			paymentModeRepository.save(existingPaymentMode);

			response.setStatus(true);
			response.setMessage("Payment mode updated successfully.");
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
