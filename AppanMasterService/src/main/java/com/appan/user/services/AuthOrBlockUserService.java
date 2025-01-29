package com.appan.user.services;

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
import com.appan.entity.SmsMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.SmsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.user.model.UserAuthOrBlockRequest;
import com.appan.utils.CommonUtils;
import com.appan.utils.MyUtils;
import com.google.gson.GsonBuilder;

@Service
public class AuthOrBlockUserService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private SmsMasterRepository smsMasterRepository;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private MyUtils dbUtils;

	@Value("${SMS_KEY}")
	private String smsKey;

	@Value("${SMS_FROM}")
	private String smsFrom;

	@Value("${USER_CODE}")
	private String userCode;

	private static final Logger Logger = LoggerFactory.getLogger(AuthOrBlockUserService.class);
	private static final ThreadLocal<SimpleDateFormat> rrnSuffixFormat = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyyDDDHH"));
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	public CommonResponse authorblockUser(UserAuthOrBlockRequest req) {
		Logger.info("\r\n\r\n**************************** AUTH OR BLOCK USER *************************************");
		CommonResponse response = new CommonResponse();
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			UserMaster ms = masterRepository.findByUserId(req.getUserId().toUpperCase());
			if (ms == null) {
				response.setStatus(false);
				response.setMessage("User " + ErrorMessages.NOT_FOUND);
				response.setRespCode("01");
				return response;
			}

			// Check if the username and userId are the same to prevent self-authorization
			if (req.getUsername().equalsIgnoreCase(req.getUserId())) {
				response.setStatus(false);
				response.setMessage("You cannot authorize your own account.");
				response.setRespCode("07");
				return response;
			}

			// Check if user is already authorized
			if ("2".equalsIgnoreCase(ms.getAuthStatus()) && req.getStatus().equalsIgnoreCase("2")) {
				response.setStatus(false);
				response.setMessage("User is already authorized.");
				response.setRespCode("06");
				return response;
			}

			// Check if user is already blocked
			if ("3".equalsIgnoreCase(ms.getAuthStatus()) && req.getStatus().equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("User is already blocked.");
				response.setRespCode("08");
				return response;
			}

			String newPass = "S" + req.getUserId().toLowerCase().substring(0, 3) + "@" + generateOTP();

			String pass = commonUtils.hashSHA256(req.getUserId().toUpperCase(), newPass);
			Date passExDt = dbUtils.getExpiryDt();
			ms.setAuthBy(req.getUsername().toUpperCase());
			ms.setAuthDt(new Date());

			if (req.getStatus().equalsIgnoreCase("2")) {
				ms.setAuthStatus("2");
				ms.setPasswd(pass);
				ms.setPasswdExp(passExDt);

				response.setMessage("User " + ErrorMessages.AUTH_SUCCESS);

				String txnId = userCode + getTxnId();
				SmsMaster sms = new SmsMaster();
				sms.setMobileNo(ms.getMobileNo());
				sms.setUsername(userCode);
				sms.setOtpDate(new Date());
				sms.setSms(ErrorMessages.USER_CREATION_PASSWORD.replace("<USERNAME>", req.getUserId().toUpperCase())
						.replace("<PASSWORD>", newPass));
				sms.setStatus("P");
				sms.setSmsKey(smsKey);
				sms.setSmsFrom(smsFrom);
				sms.setTemplateId("1707172992507471784");
				sms.setEntityId("1201160819143415278");
				sms.setSmsResponse("SMS send pending for proccess");
				sms.setSendTxnId(txnId);
				smsMasterRepository.save(sms);

			} else if (req.getStatus().equalsIgnoreCase("3")) {
				ms.setAuthStatus("3");
				response.setMessage("User " + ErrorMessages.BLOCK_SUCCESS);
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage(ErrorMessages.INVALID_STATUS);
				return response;
			}

			masterRepository.save(ms);
			response.setStatus(true);
			response.setRespCode("00");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
			return response;
		}
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