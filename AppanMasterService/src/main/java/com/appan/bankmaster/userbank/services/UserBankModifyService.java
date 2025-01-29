package com.appan.bankmaster.userbank.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.entity.UserBank;
import com.appan.bankmaster.userbank.model.UserBankModifyRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserBankRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

@Service
public class UserBankModifyService {

	@Value("${documents_url}")
	private String documentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserBankRepository userBankRepository;

	public CommonResponse modifyUserBank(UserBankModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			UserBank userBank = userBankRepository.findById(req.getId()).orElse(null);

			if (userBank == null) {
				response.setStatus(false);
				response.setMessage("UserBank entry with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			UserBank existingUserBank = userBankRepository.findByAccNumberAndBankCode(req.getAccNumber(),
					req.getBankCode());

			if (existingUserBank != null && !existingUserBank.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("UserBank entry with the same account number and bank code already exists.");
				response.setRespCode("03");
				return response;
			}

			if (req.getFile() != null && !req.getFile().isEmpty()) {
				response = myUtils.saveImageToDisk(req.getFile(), "file.png",
						serverDocPath + "bank/userbank/" + req.getId());
				if (!response.isStatus()) {
					return response;
				}

				String filePath = documentsUrl + "?docPath=bank/userbank?id=" + req.getId() + "&fileName=file.png";
				userBank.setFile(filePath);
			}

			userBank.setUsername(req.getUserName());
			userBank.setBankName(req.getBankName());
			userBank.setBankCode(req.getBankCode());
			userBank.setAccName(req.getAccName());
			userBank.setAccNumber(req.getAccNumber());
			userBank.setIfscCode(req.getIfscCode());
			userBank.setBranch(req.getBranch());
			userBank.setUpiId(req.getUpiId());
			userBank.setType(req.getType());
			userBank.setAccType(req.getAccType());
			userBank.setPriority(req.getPriority());
			userBank.setIsActive(req.getIsActive());
			userBank.setModifyBy(req.getUsername().toUpperCase());
			userBank.setModifyDt(new Date());

			userBankRepository.save(userBank);

			response.setStatus(true);
			response.setMessage("UserBank entry modified successfully.");
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
