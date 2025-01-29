package com.appan.bankmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.companybankaccount.model.CompanyBankAccountAuthOrBlockRequest;
import com.appan.bankmaster.entity.CompanyBankAccount;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CompanyBankAccountRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class CompanyBankAccountAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CompanyBankAccountRepository companyBankAccountRepository;

	private static final Logger logger = LoggerFactory.getLogger(CompanyBankAccountAuthOrBlockService.class);

	public CommonResponse authorBlockCompanyBankAccount(CompanyBankAccountAuthOrBlockRequest req) {
		logger.info(
				"\r\n\r\n**************************** AUTH OR BLOCK COMPANY BANK ACCOUNT *************************************");

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			CompanyBankAccount account = companyBankAccountRepository.findById(req.getId()).orElse(null);

			if (account == null) {
				response.setStatus(false);
				response.setMessage("Company Bank Account ID not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("1") && account.getAuthStatus().equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Account is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && account.getAuthStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Account is already blocked.");
				response.setRespCode("01");
				return response;
			}

			account.setAuthBy(req.getUsername().toUpperCase());
			account.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				account.setAuthStatus("1");
				response.setMessage("Company Bank Account authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				account.setAuthStatus("3");
				response.setMessage("Company Bank Account blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			companyBankAccountRepository.save(account);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("Exception occurred while processing the request.");
			response.setRespCode("EX");
			return response;
		}
	}
}
