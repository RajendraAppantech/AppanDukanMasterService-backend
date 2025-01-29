package com.appan.usermanagement.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.entity.UserMenu;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.repositories.Repositories.UsernameFormatRepository;
import com.appan.usermanagement.models.CreateUserManagementRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;
import com.appan.utils.MyUtils;
import com.google.common.base.Strings;

import jakarta.validation.Valid;

@Service
public class CreateUserManagementService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Value("${user_default_tpin}")
	private String defaultTpin;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserManagementMasterRepository userManagementMasterRepository;

	@Autowired
	private UsernameFormatRepository usernameFormatRepository;

	@Autowired
	private UserMenuRepository menuRepository;

	public CommonResponse create(@Valid CreateUserManagementRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			String userType = req.getUserType();
			String prefix = getPrefixFromUserType(userType);
			System.out.println("prefix" + prefix);

			if (prefix == null) {
				response.setStatus(false);
				response.setMessage("Invalid user type, prefix not found.");
				response.setRespCode("02");
				return response;
			}

			String customUserId = generateCustomUserId(req.getUserType(), prefix);

			UserManagementMaster managementMaster = new UserManagementMaster();
			managementMaster.setUserType(req.getUserType());
			managementMaster.setCode(req.getCode());
			managementMaster.setEntityType(req.getEntityType());
			managementMaster.setUserAgreement(req.getUserAgreement());
			managementMaster.setFullName(req.getFullName());
			managementMaster.setMobileNumber(req.getMobileNumber());
			managementMaster.setEmailId(req.getEmailId());
			managementMaster.setProfile(req.getProfile());
			managementMaster.setParent(req.getParent());
			managementMaster.setStatus("Active");
			managementMaster.setWalletStatus("Unfreeze");
			managementMaster.setKycStatus("Registered");
			managementMaster.setParentId(req.getParentId());
			managementMaster.setParentName(req.getParentName());
			managementMaster.setUserId(customUserId);
			managementMaster.setCreatedBy(req.getUsername().toUpperCase());
			managementMaster.setCreatedDt(new Date());

			if (req.getAddressRequest() != null) {
				managementMaster.setAddress(req.getAddressRequest().getAddress());
				managementMaster.setPincode(req.getAddressRequest().getPincode());
				managementMaster.setState(req.getAddressRequest().getState());
				managementMaster.setCity(req.getAddressRequest().getCity());
				managementMaster.setBlockPo(req.getAddressRequest().getBlockPo());
			}

			if (req.getBusinessDetailsRequest() != null) {
				managementMaster.setFirmShopName(req.getBusinessDetailsRequest().getFirmShopName());
				managementMaster.setBusinessAddress(req.getBusinessDetailsRequest().getBusinessAddress());
			}

			if (req.getVerificationDetailsRequest() != null) {
				managementMaster.setIdProof(req.getVerificationDetailsRequest().getIdProof());
				managementMaster.setPanAadharCardNo(req.getVerificationDetailsRequest().getPanAadharCardNo());
				managementMaster.setPanAadharName(req.getVerificationDetailsRequest().getPanAadharName());
				managementMaster.setBankVerification(req.getVerificationDetailsRequest().getBankVerification());
				managementMaster.setAccountHolderName(req.getVerificationDetailsRequest().getAccountHolderName());
				managementMaster.setBankName(req.getVerificationDetailsRequest().getBankName());
				managementMaster.setIfsc(req.getVerificationDetailsRequest().getIfsc());
				managementMaster.setAccountNumber(req.getVerificationDetailsRequest().getAccountNumber());
				managementMaster.setAddressProof(req.getVerificationDetailsRequest().getAddressProof());
				managementMaster.setAadharNo(req.getVerificationDetailsRequest().getAadharNo());
				managementMaster.setAadharNo(req.getVerificationDetailsRequest().getAadharNo());
				managementMaster.setNameAsPerAadhar(req.getVerificationDetailsRequest().getNameAsPerAadhar());

				managementMaster.setPaymentMethod(req.getVerificationDetailsRequest().getPaymentMethod());
				managementMaster.setUpiId(req.getVerificationDetailsRequest().getUpiId());
				managementMaster.setAccountType(req.getVerificationDetailsRequest().getAccountType());

				UserManagementMaster ms = userManagementMasterRepository.findTopByOrderByIdDesc();

				Long newId = ms != null ? ms.getId() + 1 : 1L;

				if (!Strings.isNullOrEmpty(req.getVerificationDetailsRequest().getPanAadharUploadFile())) {
					response = myUtils.saveImageToDisk(req.getVerificationDetailsRequest().getPanAadharUploadFile(),"pan_aadhar.png", serverDocPath + "user/management/" + newId);
					if (!response.isStatus())
						return response;
					managementMaster.setPanAadharUploadFile(
							doccumentsUrl + "?docPath=user/management/&id=" + newId + "&fileName=pan_aadhar.png");
				}

				if (!Strings.isNullOrEmpty(req.getVerificationDetailsRequest().getBankAccountUploadFile())) {
					response = myUtils.saveImageToDisk(req.getVerificationDetailsRequest().getBankAccountUploadFile(),
							"bank_account.png", serverDocPath + "user/management/" + newId);
					if (!response.isStatus())
						return response;
					managementMaster.setBankAccountUploadFile(
							doccumentsUrl + "?docPath=user/management/&id=" + newId + "&fileName=bank_account.png");
				}

				if (!Strings.isNullOrEmpty(req.getVerificationDetailsRequest().getAadharFrontUploadFile())) {
					response = myUtils.saveImageToDisk(req.getVerificationDetailsRequest().getAadharFrontUploadFile(),
							"aadhar_front.png", serverDocPath + "user/management/" + newId);
					if (!response.isStatus())
						return response;
					managementMaster.setAadharFrontUploadFile(
							doccumentsUrl + "?docPath=user/management/&id=" + newId + "&fileName=aadhar_front.png");
				}

				if (!Strings.isNullOrEmpty(req.getVerificationDetailsRequest().getAadharBackUploadFile())) {
					response = myUtils.saveImageToDisk(req.getVerificationDetailsRequest().getAadharBackUploadFile(),
							"aadhar_back.png", serverDocPath + "user/management/" + newId);
					if (!response.isStatus())
						return response;
					managementMaster.setAadharBackUploadFile(
							doccumentsUrl + "?docPath=user/management/&id=" + newId + "&fileName=aadhar_back.png");
				}
			}

			userManagementMasterRepository.save(managementMaster);

			UserMenu userMenu = menuRepository.findByUserRoleAndUserProfile(req.getUserType(), req.getProfile());
			String menu = userMenu != null ? userMenu.getMenu() : "";

			UserMaster ms = new UserMaster();

			ms.setUserId(customUserId);
			ms.setName(req.getFullName());
			ms.setMobileNo(req.getMobileNumber());
			ms.setEmailId(req.getEmailId());
			ms.setUserRole(req.getUserType());
			ms.setUserProfile(req.getProfile());
			ms.setStatus("Active");
			ms.setUserMenu(menu);
			ms.setUserCode(req.getCode());
			ms.setPasswd("0000000000000000000000000000000000000000000000000000000000000000");
			ms.setTpin(defaultTpin);
			ms.setCreatedBy(req.getUsername().toUpperCase());
			ms.setCreatedDt(new Date());
			ms.setAuthStatus("4");

			userMasterRepository.save(ms);

			response.setStatus(true);
			response.setMessage("User management details added successfully.");
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

	private String getPrefixFromUserType(String userType) {
		return usernameFormatRepository.findPrefixByUserType(userType);
	}

	private String generateCustomUserId(String userType, String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException("Prefix cannot be null.");
		}

		if (userType == null) {
			throw new IllegalArgumentException("User type cannot be null.");
		}

		String lastUserId = userManagementMasterRepository.findLastUserIdByUserType(userType);

		String numberPart = "000000";
		if (lastUserId != null && lastUserId.length() > 6) {
			numberPart = lastUserId.substring(lastUserId.length() - 6);
		}

		int currentNumber = Integer.parseInt(numberPart);
		currentNumber++;

		String newNumberPart = String.format("%06d", currentNumber);

		return prefix + newNumberPart;
	}

}
