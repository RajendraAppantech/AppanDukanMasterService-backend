package com.appan.usermanagement.services;

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
public class CreateNewUserKycService {

	@Value("${documents_url}")
	private String documentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private UserManagementMasterRepository userManagementMasterRepository;

	@Autowired
	private MyUtils myUtils;

	public CommonResponse create(@Valid CreateNewUserKycRequest req) {
		CommonResponse response = new CommonResponse();

		response.setStatus(true);
		try {
			UserManagementMaster existingUser = userManagementMasterRepository.findByUserId(req.getUserId());
			if (existingUser == null) {
				response.setStatus(false);
				response.setMessage("User with the given userId does not exist.");
				response.setRespCode("01");
				return response;
			}

			UserManagementMaster ms = userManagementMasterRepository.findTopByOrderByIdDesc();

			Long newId = ms != null ? ms.getId() + 1 : 1L;

			if (!Strings.isNullOrEmpty(req.getPanDocumentUploadFile())) {
				response = myUtils.saveImageToDisk(req.getPanDocumentUploadFile(), "pan_document.png",
						serverDocPath + "user/kyc/" + newId);

				if (!response.isStatus())
					return response;
				existingUser.setPanDocumentUploadFile(
						documentsUrl + "?docPath=user/kyc/&id=" + existingUser.getId() + "&fileName=pan_document.png");
			}

			if (!Strings.isNullOrEmpty(req.getBankAccountUploadFile())) {

				response = myUtils.saveImageToDisk(req.getBankAccountUploadFile(), "bank_account.png",
						serverDocPath + "user/kyc/" + newId);

				if (!response.isStatus())
					return response;

				existingUser.setBankAccountUploadFile(
						documentsUrl + "?docPath=user/kyc/&id=" + existingUser.getId() + "&fileName=bank_account.png");
			}

			if (!Strings.isNullOrEmpty(req.getAadharFrontUploadFile())) {

				response = myUtils.saveImageToDisk(req.getAadharFrontUploadFile(), "aadhar_front.png",
						serverDocPath + "user/kyc/" + newId);

				if (!response.isStatus())
					return response;
				existingUser.setAadharFrontUploadFile(
						documentsUrl + "?docPath=user/kyc/&id=" + existingUser.getId() + "&fileName=aadhar_front.png");
			}

			if (!Strings.isNullOrEmpty(req.getAadharBackUploadFile())) {
				response = myUtils.saveImageToDisk(req.getAadharBackUploadFile(), "aadhar_back.png",
						serverDocPath + "user/kyc/" + newId);

				if (!response.isStatus())
					return response;
				existingUser.setAadharBackUploadFile(
						documentsUrl + "?docPath=user/kyc/&id=" + existingUser.getId() + "&fileName=aadhar_back.png");
			}

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

			userManagementMasterRepository.save(existingUser);

			response.setStatus(true);
			response.setMessage("User KYC details updated successfully.");
			response.setRespCode("00");
			response.setData("userDetails", existingUser);
			return response;

		} catch (

		Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("An error occurred while updating user details.");
			response.setRespCode("03");
			return response;
		}
	}
}
