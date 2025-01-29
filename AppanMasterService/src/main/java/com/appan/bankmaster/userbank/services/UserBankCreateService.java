package com.appan.bankmaster.userbank.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.entity.UserBank;
import com.appan.bankmaster.userbank.model.UserBankCreateRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserBankRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class UserBankCreateService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserBankRepository userBankRepository;

	public CommonResponse createUserBank(@Valid UserBankCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<UserBank> existingBank = userBankRepository.findByAccNumberAndIfscCode(req.getAccNumber(),
					req.getIfscCode());
			if (existingBank.isPresent()) {
				response.setStatus(false);
				response.setMessage("Bank account already exists");
				response.setRespCode("01");
				return response;
			}

			UserBank ms = userBankRepository.findTopByOrderByIdDesc();

			Long newId = ms.getId() + 1;

			response = myUtils.saveImageToDisk(req.getFile(), "file.png", serverDocPath + "bank/userbank/" + newId);
			if (!response.isStatus()) {
				return response;
			}

			String imgPath = doccumentsUrl + "?docPath=bank/userbank?id=" + newId + "&fileName=file.png";

			UserBank newBank = new UserBank();
			newBank.setBankName(req.getBankName());
			newBank.setBankCode(req.getBankCode());
			newBank.setAccName(req.getAccName());
			newBank.setAccNumber(req.getAccNumber());
			newBank.setIfscCode(req.getIfscCode());
			newBank.setBranch(req.getBranch());
			newBank.setUpiId(req.getUpiId());
			newBank.setType(req.getType());
			newBank.setAccType(req.getAccType());
			newBank.setUsername(req.getUserName());
			newBank.setFile(imgPath);
			newBank.setPriority(req.getPriority());
			newBank.setIsActive(req.getIsActive());
			newBank.setCreatedBy(req.getUsername().toUpperCase());
			newBank.setCreatedDt(new Date());
			newBank.setAuthStatus("4");

			userBankRepository.save(newBank);

			response.setStatus(true);
			response.setMessage("Bank account created successfully");
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
