package com.appan.usermanagement.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.usermanagement.models.ModifyAddressRequest;
import com.appan.usermanagement.models.ModifyBusinessDetailsRequest;
import com.appan.usermanagement.models.ModifyUserManagementRequest;
import com.appan.usermanagement.models.ModifyVerificationDetailsRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;
import com.appan.utils.MyUtils;
import com.google.common.base.Strings;

import jakarta.validation.Valid;

@Service
public class ModifyUserManagementService {

	@Autowired
	private MyUtils myUtils;

	@Value("${documents_url}")
	private String documentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserManagementMasterRepository userManagementMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyUserManagementService.class);

	public CommonResponse modify(@Valid ModifyUserManagementRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			response.setStatus(true);
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserManagementMaster existingUser = userManagementMasterRepository.findById(req.getId()).orElse(null);
			if (existingUser == null) {
				response.setStatus(false);
				response.setMessage("User entry with the given ID not found.");
				response.setRespCode("02");
				return response;
			}

			ModifyAddressRequest addressRequest = req.getModifyAddressRequest();
			if (addressRequest != null) {
				existingUser.setAddress(addressRequest.getAddress());
				existingUser.setPincode(addressRequest.getPincode());
				existingUser.setState(addressRequest.getState());
				existingUser.setCity(addressRequest.getCity());
				existingUser.setBlockPo(addressRequest.getBlockPo());
			}

			ModifyBusinessDetailsRequest businessRequest = req.getModifyBusinessDetailsRequest();
			if (businessRequest != null) {

				existingUser.setFirmShopName(businessRequest.getFirmShopName());
				existingUser.setBusinessAddress(businessRequest.getBusinessAddress());
			}

			ModifyVerificationDetailsRequest verificationRequest = req.getModifyVerificationDetailsRequest();
			if (verificationRequest != null) {

				updateVerificationDetails(existingUser, verificationRequest, response);
				if (!response.isStatus()) {
					return response;
				}
			}

			existingUser.setFullName(req.getFullName());
			existingUser.setMobileNumber(req.getMobileNumber());
			existingUser.setEmailId(req.getEmailId());
			existingUser.setUserAgreement(req.getUserAgreement());
			existingUser.setStatus(req.getStatus());
			existingUser.setRemark(req.getRemark());
			existingUser.setModifyBy(req.getUsername().toUpperCase());
			existingUser.setModifyDt(new Date());
			existingUser.setIsEmailVerified(true);
			existingUser.setIsMobileVerified(true);

			userManagementMasterRepository.save(existingUser);

			response.setStatus(true);
			response.setMessage("User data modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Error modifying user entry: ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while modifying the user entry.");
			response.setRespCode("03");
			return response;
		}
	}
	

	private void updateVerificationDetails(UserManagementMaster existingUser,
			ModifyVerificationDetailsRequest verificationRequest, CommonResponse response) {
		try {
			if (!Strings.isNullOrEmpty(verificationRequest.getPanAadharUploadFile())) {
				existingUser.setPanAadharUploadFile(saveOrUpdateFile(verificationRequest.getPanAadharUploadFile(),
						"pan_aadhar.png", existingUser.getId(), response));
				if (!response.isStatus())
					return;
			}

			if (!Strings.isNullOrEmpty(verificationRequest.getBankAccountUploadFile())) {
				existingUser.setBankAccountUploadFile(saveOrUpdateFile(verificationRequest.getBankAccountUploadFile(),
						"bank_account.png", existingUser.getId(), response));
				if (!response.isStatus())
					return;
			}

			if (!Strings.isNullOrEmpty(verificationRequest.getAadharFrontUploadFile())) {
				existingUser.setAadharFrontUploadFile(saveOrUpdateFile(verificationRequest.getAadharFrontUploadFile(),
						"aadhar_front.png", existingUser.getId(), response));
				if (!response.isStatus())
					return;
			}

			if (!Strings.isNullOrEmpty(verificationRequest.getAadharBackUploadFile())) {
				existingUser.setAadharBackUploadFile(saveOrUpdateFile(verificationRequest.getAadharBackUploadFile(),
						"aadhar_back.png", existingUser.getId(), response));
				if (!response.isStatus())
					return;
			}

			existingUser.setIdProof(verificationRequest.getIdProof());
			existingUser.setPanAadharCardNo(verificationRequest.getPanAadharCardNo());
			existingUser.setBankVerification(verificationRequest.getBankVerification());
			existingUser.setAccountHolderName(verificationRequest.getAccountHolderName());

			existingUser.setBankName(verificationRequest.getBankName());
			existingUser.setIfsc(verificationRequest.getIfsc());
			existingUser.setAccountNumber(verificationRequest.getAccountNumber());
			existingUser.setAddressProof(verificationRequest.getAddressProof());
			existingUser.setAadharNo(verificationRequest.getAadharNo());
			existingUser.setNameAsPerAadhar(verificationRequest.getNameAsPerAadhar());
			existingUser.setPaymentMethod(verificationRequest.getPaymentMethod());
			existingUser.setUpiId(verificationRequest.getUpiId());
			existingUser.setAccountType(verificationRequest.getAccountType());
		} catch (Exception e) {
			logger.error("Error updating verification details: ", e);
			response.setStatus(false);
			response.setMessage("Failed to update verification details.");
		}
	}

	private String saveOrUpdateFile(String fileContent, String fileName, Long userId, CommonResponse response) {
		try {
			if (fileContent.startsWith("http") || fileContent.startsWith("https")) {
				return fileContent;
			} else {
				response = myUtils.saveImageToDisk(fileContent, fileName, serverDocPath + "user/management/" + userId);
				if (!response.isStatus()) {
					return null;
				}
				return documentsUrl + "?docPath=user/management&id=" + userId + "&fileName=" + fileName;
			}
		} catch (Exception e) {
			logger.error("Error saving file: ", e);
			response.setStatus(false);
			response.setMessage("Error saving file: " + fileName);
			return null;

		}
	}
}
