package com.appan.usermanagement.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserManagementMaster;
import com.appan.usermanagement.models.CreateNewUserKycRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;
import com.appan.utils.MyUtils;
import com.google.common.base.Strings;

import jakarta.validation.Valid;

@Service
public class ModifyUserManagKycService {

	@Autowired
	private MyUtils myUtils;

	@Value("${documents_url}")
	private String documentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private UserManagementMasterRepository userManagementMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyUserManagKycService.class);

	public CommonResponse modify(@Valid CreateNewUserKycRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserManagementMaster existingUser = userManagementMasterRepository.findByUserId(req.getUserId());
			response.setStatus(true);
			if (existingUser == null) {
				response.setStatus(false);
				response.setMessage("User with the given userId does not exist.");
				response.setRespCode("01");
				return response;
			}

			updateUserDetails(existingUser, req);

			updateVerificationFiles(existingUser, req, response);
			if (!response.isStatus()) {
				return response;
			}

			existingUser.setModifyDt(new Date());
			userManagementMasterRepository.save(existingUser);

			response.setStatus(true);
			response.setMessage("User KYC details updated successfully.");
			response.setRespCode("00");
			response.setData("userDetails", existingUser);
			return response;

		} catch (Exception e) {
			logger.error("Error modifying user KYC details: ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while updating user details.");
			response.setRespCode("03");
			return response;
		}
	}

	private void updateUserDetails(UserManagementMaster existingUser, CreateNewUserKycRequest req) {
		existingUser.setIdProof(req.getIdProof());
		existingUser.setPancardNo(req.getPancardNo());
		existingUser.setPassportNumber(req.getPassportNumber());
		existingUser.setBankVerification(req.getBankVerification());
		existingUser.setAccountHolderName(req.getAccountHolderName());
		existingUser.setBankName(req.getBankName());
		existingUser.setIfsc(req.getIfsc());
		existingUser.setAccountNumber(req.getAccountNumber());
		existingUser.setAddressProof(req.getAddressProof());
		existingUser.setAadharNo(req.getAadharNo());
		existingUser.setAccountType(req.getAccountType());
		existingUser.setUpiId(req.getUpiId());
		existingUser.setPaymentMethod(req.getPaymentMethod());
	}

	private void updateVerificationFiles(UserManagementMaster existingUser, CreateNewUserKycRequest req,
			CommonResponse response) {

		if (!Strings.isNullOrEmpty(req.getPanDocumentUploadFile())) {
			existingUser.setPanDocumentUploadFile(saveOrUpdateFile(req.getPanDocumentUploadFile(), "pan_document.png",
					existingUser.getId(), response));
			if (!response.isStatus())
				return;
		}

		if (!Strings.isNullOrEmpty(req.getBankAccountUploadFile())) {
			existingUser.setBankAccountUploadFile(saveOrUpdateFile(req.getBankAccountUploadFile(), "bank_account.png",
					existingUser.getId(), response));
			if (!response.isStatus())
				return;

		}

		if (!Strings.isNullOrEmpty(req.getAadharFrontUploadFile())) {
			existingUser.setAadharFrontUploadFile(saveOrUpdateFile(req.getAadharFrontUploadFile(), "aadhar_front.png",
					existingUser.getId(), response));
			if (!response.isStatus())
				return;
		}

		if (!Strings.isNullOrEmpty(req.getAadharBackUploadFile())) {
			existingUser.setAadharBackUploadFile(
					saveOrUpdateFile(req.getAadharBackUploadFile(), "aadhar_back.png", existingUser.getId(), response));

		}
	}

	private String saveOrUpdateFile(String fileContent, String fileName, Long userId, CommonResponse response) {
		try {
			if (fileContent.startsWith("http") || fileContent.startsWith("https")) {
				return fileContent;
			}

			response = myUtils.saveImageToDisk(fileContent, fileName, serverDocPath + "user/kyc/" + userId);
			if (!response.isStatus()) {
				return null;
			}

			return documentsUrl + "?docPath=user/kyc&id=" + userId + "&fileName=" + fileName;

		} catch (Exception e) {
			logger.error("Error saving file {}: ", fileName, e);
			response.setStatus(false);
			response.setMessage("Error saving file: " + fileName);
			return null;
		}
	}
}
