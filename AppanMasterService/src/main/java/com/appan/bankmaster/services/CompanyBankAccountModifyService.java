package com.appan.bankmaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.companybankaccount.model.CompanyBankAccountModifyRequest;
import com.appan.bankmaster.entity.CompanyBankAccount;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CompanyBankAccountRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class CompanyBankAccountModifyService {

	@Value("${documents_url}")
	private String documentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CompanyBankAccountRepository companyBankAccountRepository;

	public CommonResponse modifyCompanyBankAccount(@Valid CompanyBankAccountModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			Optional<CompanyBankAccount> existingAccount = companyBankAccountRepository.findById(req.getId());
			if (existingAccount.isEmpty()) {
				response.setStatus(false);
				response.setMessage("Record with ID " + req.getId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			if (req.getFile() != null && !req.getFile().isEmpty()) {
				response = myUtils.saveImageToDisk(req.getFile(), "file.png",
						serverDocPath + "bank/companybankaccount/" + req.getId());
				if (!response.isStatus()) {
					return response;
				}

				String filePath = documentsUrl + "?docPath=bank/companybankaccount?id=" + req.getId()
						+ "&fileName=file.png";
				existingAccount.get().setFile(filePath);
			}

			CompanyBankAccount accountToUpdate = existingAccount.get();
			accountToUpdate.setBankName(req.getBankName());
			accountToUpdate.setBankCode(req.getBankCode());
			accountToUpdate.setAccName(req.getAccName());
			accountToUpdate.setAccNumber(req.getAccNumber());
			accountToUpdate.setIfscCode(req.getIfscCode());
			accountToUpdate.setBranch(req.getBranch());
			accountToUpdate.setUpiId(req.getUpiId());
			accountToUpdate.setType(req.getType());
			accountToUpdate.setAccType(req.getAccType());
			accountToUpdate.setUsername(req.getUserName());
			accountToUpdate.setPriority(req.getPriority());
			accountToUpdate.setIsActive(req.getIsActive());
			accountToUpdate.setModifyBy(req.getUsername().toUpperCase());
			accountToUpdate.setModifyDt(new Date());

			companyBankAccountRepository.save(accountToUpdate);

			response.setStatus(true);
			response.setMessage("Company Bank Account updated successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage("An error occurred while updating the Company Bank Account.");
			response.setRespCode("EX");
			return response;
		}
	}
}
