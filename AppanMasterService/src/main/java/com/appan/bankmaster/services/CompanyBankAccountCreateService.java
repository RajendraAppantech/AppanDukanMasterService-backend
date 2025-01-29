package com.appan.bankmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.companybankaccount.model.CompanyBankAccountCreateRequest;
import com.appan.bankmaster.entity.CompanyBankAccount;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CompanyBankAccountRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class CompanyBankAccountCreateService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CompanyBankAccountRepository companyBankAccountRepository;

	private static final Logger logger = LoggerFactory.getLogger(CompanyBankAccountCreateService.class);

	public CommonResponse createCompanyBankAccount(@Valid CompanyBankAccountCreateRequest req) {
		logger.info(
				"\r\n\r\n**************************** CREATE CompanyBankAccount *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			boolean isDuplicate = companyBankAccountRepository.existsByAccNumberAndBankName(req.getAccNumber(),
					req.getBankName());
			if (isDuplicate) {
				logger.error("Duplicate data found for account number '{}' and bank name '{}'", req.getAccNumber(),
						req.getBankName());
				response.setStatus(false);
				response.setMessage("Duplicate data: Company bank account already exists");
				response.setRespCode("01");
				return response;
			}

			CompanyBankAccount ms = companyBankAccountRepository.findTopByOrderByIdDesc();

			Long newId = ms.getId() + 1;

			response = myUtils.saveImageToDisk(req.getFile(), "file.png",
					serverDocPath + "bank/companybankaccount/" + newId);
			if (!response.isStatus()) {
				return response;
			}

			String imgPath = doccumentsUrl + "?docPath=bank/companybankaccount?id=" + newId + "&fileName=file.png";

			CompanyBankAccount companyBankAccount = new CompanyBankAccount();
			companyBankAccount.setUsername(req.getUserName());
			companyBankAccount.setBankName(req.getBankName());
			companyBankAccount.setBankCode(req.getBankCode());
			companyBankAccount.setAccName(req.getAccName());
			companyBankAccount.setAccNumber(req.getAccNumber());
			companyBankAccount.setIfscCode(req.getIfscCode());
			companyBankAccount.setBranch(req.getBranch());
			companyBankAccount.setUpiId(req.getUpiId());
			companyBankAccount.setType(req.getType());
			companyBankAccount.setAccType(req.getAccType());
			companyBankAccount.setFile(imgPath);
			companyBankAccount.setPriority(req.getPriority());
			companyBankAccount.setIsActive(req.getIsActive());
			companyBankAccount.setCreatedBy(req.getUsername().toUpperCase());
			companyBankAccount.setCreatedDt(new Date());
			companyBankAccount.setAuthStatus("4");

			companyBankAccountRepository.save(companyBankAccount);

			response.setStatus(true);
			response.setMessage("Company Bank Account created successfully.");
			response.setRespCode("00");

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while creating the Company Bank Account.");
			response.setRespCode("EX");
		}

		return response;
	}
}
