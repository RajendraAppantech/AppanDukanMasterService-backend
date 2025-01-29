package com.appan.usermanagement.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.RetailerUserMaster;
import com.appan.entity.SmsMaster;
import com.appan.entity.UserManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.entity.UserMenu;
import com.appan.repositories.Repositories.RetailerUserMasterRepository;
import com.appan.repositories.Repositories.SmsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.repositories.Repositories.UsernameFormatRepository;
import com.appan.user.services.AuthOrBlockUserService;
import com.appan.usermanagement.models.CreateNewUserRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;
import com.appan.utils.CommonUtils;
import com.appan.utils.DBUtils;

import jakarta.validation.Valid;

@Service
public class CreateNewUserService {

	@Value("${SMS_KEY}")
	private String smsKey;

	@Value("${SMS_FROM}")
	private String smsFrom;

	@Value("${USER_CODE}")
	private String userCode;

	@Value("${documents_url}")
	private String documentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Value("${user_default_tpin}")
	private String defaultTpin;

	@Autowired
	private DBUtils dbUtils;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private UserMenuRepository menuRepository;

	@Autowired
	private SmsMasterRepository smsMasterRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UsernameFormatRepository usernameFormatRepository;

	@Autowired
	private RetailerUserMasterRepository retailerUserMasterRepository;

	@Autowired
	private UserManagementMasterRepository userManagementMasterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(AuthOrBlockUserService.class);
	private static final ThreadLocal<SimpleDateFormat> rrnSuffixFormat = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyyDDDHH"));
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	public CommonResponse create(@Valid CreateNewUserRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			
			  // Check if mobile number already exists
	        boolean mobileNumberExists = userManagementMasterRepository.existsByMobileNumber(req.getMobileNumber());
	        if (mobileNumberExists) {
	            response.setStatus(false);
	            response.setMessage("Mobile number already exists.");
	            response.setRespCode("05");
	            return response;
	        }

			String prefix = getPrefixFromUserType(req.getUserType());
			if (prefix == null) {
				response.setStatus(false);
				response.setMessage("Invalid user type, prefix not found.");
				response.setRespCode("02");
				return response;
			}

			String customUserId = generateCustomUserId(req.getUserType(), prefix);

			UserManagementMaster managementMaster = new UserManagementMaster();
			managementMaster.setUserId(customUserId);
			managementMaster.setUserType(req.getUserType());
			managementMaster.setCode(req.getCode());
			managementMaster.setEntityType(req.getEntityType());
			managementMaster.setFullName(req.getFullName());
			managementMaster.setMobileNumber(req.getMobileNumber());
			managementMaster.setEmailId(req.getEmailId());
			managementMaster.setAddress(req.getAddress());
			managementMaster.setProfile(req.getProfile());
			managementMaster.setParent(req.getParent());
			managementMaster.setParentId(req.getParentId());
			managementMaster.setParentName(req.getParentName());
			managementMaster.setPincode(req.getPincode());
			managementMaster.setState(req.getState());
			managementMaster.setCity(req.getCity());
			managementMaster.setBlockPo(req.getBlockPo());
			managementMaster.setStatus("Active");
			managementMaster.setCreatedBy(req.getUsername().toUpperCase());
			managementMaster.setCreatedDt(new Date());

			userManagementMasterRepository.save(managementMaster);

			// Fetch user menu based on userMenuType
			UserMenu userMenu = menuRepository.findByRoleName(req.getUserMenuType());
			if (userMenu == null) {
				response.setStatus(false);
				response.setMessage("Invalid userMenuType, menu not found.");
				response.setRespCode("04");
				return response;
			}
			String menu = userMenu.getMenu();

			String newPass = "A" + customUserId.toLowerCase().substring(0, 3) + "@" + generateOTP();

			String pass = commonUtils.hashSHA256(customUserId.toUpperCase(), newPass);
			Date passExDt = dbUtils.getExpiryDt();

			// Save UserMaster entity
			UserMaster newUser = new UserMaster();
			newUser.setUserId(customUserId);
			newUser.setName(req.getFullName());
			newUser.setMobileNo(req.getMobileNumber());
			newUser.setEmailId(req.getEmailId());
			newUser.setUserRole(req.getUserType());
			newUser.setUserProfile(req.getProfile());
			newUser.setStatus("Active");
			newUser.setUserMenu(menu);
			newUser.setUserCode(req.getCode());
			newUser.setPasswd(pass);
			newUser.setPasswdExp(passExDt);
			newUser.setTpin(defaultTpin);
			newUser.setCreatedBy(req.getUsername().toUpperCase());
			newUser.setCreatedDt(new Date());
			newUser.setAuthStatus("2");

			if (req.getUserType().equalsIgnoreCase("Retailer")) {
				RetailerUserMaster retailerUser = new RetailerUserMaster();
				retailerUser.setUserId(customUserId);
				retailerUser.setName(req.getFullName());
				retailerUser.setMobileNo(req.getMobileNumber());
				retailerUser.setEmailId(req.getEmailId());
				retailerUser.setUserRole(req.getUserType());
				retailerUser.setUserProfile(req.getProfile());
				retailerUser.setStatus("Active");
				retailerUser.setUserMenu(menu);
				retailerUser.setUserCode(req.getCode());
				retailerUser.setPasswd(pass);
				retailerUser.setPasswdExp(passExDt);
				retailerUser.setTpin(defaultTpin);
				retailerUser.setCreatedBy(req.getUsername().toUpperCase());
				retailerUser.setCreatedDt(new Date());
				retailerUser.setAuthStatus("2");

				retailerUserMasterRepository.save(retailerUser);
			} else {
//			    userMasterRepository.save(newUser);
			}

			userMasterRepository.save(newUser);

			String txnId = userCode + getTxnId();

			// Save SMS details in SmsMaster
			SmsMaster smsMaster = new SmsMaster();
			smsMaster.setMobileNo(req.getMobileNumber());
			smsMaster.setUsername(userCode);
			smsMaster.setOtpDate(new Date());
			smsMaster.setSms(ErrorMessages.USER_CREATION_PASSWORD.replace("<USERNAME>", customUserId.toUpperCase())
					.replace("<PASSWORD>", newPass));
			smsMaster.setStatus("P");
			smsMaster.setSmsKey(smsKey);
			smsMaster.setSmsFrom(smsFrom);
			smsMaster.setTemplateId("1707172992507471784");
			smsMaster.setEntityId("1201160819143415278");
			smsMaster.setSmsResponse("SMS send pending for proccess");
			smsMaster.setSendTxnId(txnId);

			smsMasterRepository.save(smsMaster);

			response.setStatus(true);
			response.setMessage("User created successfully.");
			response.setRespCode("00");
			response.setData("userDetails", managementMaster);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("An unexpected error occurred.");
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

		int currentNumber = Integer.parseInt(numberPart) + 1;
		String newNumberPart = String.format("%06d", currentNumber);

		return prefix + newNumberPart;
	}

	private String generateOTP() {
		StringBuilder generatedToken = new StringBuilder();
		try {
			SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
			for (int i = 0; i < 4; i++) {
				generatedToken.append(number.nextInt(9));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedToken.toString();
	}

	public String getTxnId() {
		return rrnSuffixFormat.get().format(new Date()).substring(3)
				+ String.format("%06d", SECURE_RANDOM.nextInt(999999));
	}
}
