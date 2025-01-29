package com.appan.paymentmaster.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SubPaymentMode;
import com.appan.entity.UserMaster;
import com.appan.paymentmaster.model.SubPaymentModeModifyRequest;
import com.appan.repositories.Repositories.SubPaymentModeRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

@Service
public class ModifySubPaymentmodeService {
	
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

	public CommonResponse modify(SubPaymentModeModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<SubPaymentMode> existingSubPaymentModeOpt = subPaymentModeRepository.findById(req.getId());
			if (!existingSubPaymentModeOpt.isPresent()) {
				response.setStatus(false);
				response.setMessage("Sub-payment mode with ID " + req.getId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			SubPaymentMode existingSubPaymentMode = existingSubPaymentModeOpt.get();

			boolean isDuplicate = subPaymentModeRepository
					.existsBySubPaymentModeIgnoreCaseAndPaymentModeIgnoreCaseAndIdNot(req.getSubPaymentMode(),
							req.getPaymentMode(), req.getId());
			if (isDuplicate) {
				response.setStatus(false);
				response.setMessage("Sub-payment mode and payment mode combination already exists.");
				response.setRespCode("02");
				return response;
			}
			
			if (req.getImage() != null && !req.getImage().isEmpty()) {
				if (!req.getImage().startsWith("http") && !req.getImage().startsWith("https")) {
					response = myUtils.saveImageToDisk(req.getImage(), "image.png",
							serverDocPath + "payment/subpaymentmode/" + existingSubPaymentMode.getId());
					if (!response.isStatus()) {
						return response;
					}

					String imgPath = doccumentsUrl + "?docPath=payment/subpaymentmode?id=" + existingSubPaymentMode.getId()
							+ "&fileName=image.png";
					existingSubPaymentMode.setImage(imgPath);
				}
			}

			existingSubPaymentMode.setSubPaymentMode(req.getSubPaymentMode());
			existingSubPaymentMode.setPaymentMode(req.getPaymentMode());
			existingSubPaymentMode.setImage(req.getImage());
			existingSubPaymentMode.setStatus(req.getStatus());

			subPaymentModeRepository.save(existingSubPaymentMode);

			response.setStatus(true);
			response.setMessage("Sub-payment mode updated successfully.");
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
