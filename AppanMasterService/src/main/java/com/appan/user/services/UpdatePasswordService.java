package com.appan.user.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.PasswordHistory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.PasswordExpRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.user.model.UpdatePasswordRequest;
import com.appan.user.model.UpdateTpinRequest;
import com.appan.utils.CommonUtils;
import com.appan.utils.MyUtils;

@Service
public class UpdatePasswordService {

	private static final Logger logger = LoggerFactory.getLogger(UpdatePasswordService.class);

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private MyUtils dbUtils;

	@Autowired
	PasswordExpRepository passwordExpRepository;

	@Transactional
	public CommonResponse updatePassword(UpdatePasswordRequest req) {
		logger.info("\r\n\r\n**************************** CHANGE PASSWORD *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage("Invalid username");
				response.setRespCode("01");
				return response;
			}

			String oldPass = commonUtils.hashSHA256(req.getUsername().toUpperCase(), req.getOldPassword());
			if (!master.getPasswd().equalsIgnoreCase(oldPass)) {
				response.setStatus(false);
				response.setMessage("Invalid Current Password");
				response.setRespCode("01");
				return response;
			}
			if (!req.getPassword().equalsIgnoreCase(req.getConfirmPassword())) {
				response.setStatus(false);
				response.setMessage("New password and confirm password must be the same.");
				response.setRespCode("01");
				return response;
			}
			String pass = commonUtils.hashSHA256(req.getUsername().toUpperCase(), req.getPassword());

			boolean passStatus = dbUtils.getLastThreePassword(req.getUsername().toUpperCase(), pass);
			if (!passStatus) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.NEW_PASS_SHOUD_BE_DIFF_LAST_3_PASS);
				response.setRespCode("01");
				return response;
			}

			Date passExDt = dbUtils.getExpiryDt();

			PasswordHistory passhisty = new PasswordHistory();
			passhisty.setUserid(req.getUsername().toUpperCase());
			passhisty.setPassword_type("USER");
			passhisty.setOld_passwd(master.getPasswd());
			passhisty.setNew_passwd(pass);
			passhisty.setChangetime(new Date());
			passwordExpRepository.save(passhisty);

			master.setPasswd(pass);
			master.setPasswdExp(passExDt);
			masterRepository.save(master);

			response.setStatus(true);
			response.setMessage(ErrorMessages.PASSWORD_CHANGE_SUCCESS);
			response.setRespCode("00");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}

	@Transactional
	public CommonResponse changeTpin(UpdateTpinRequest req) {
		logger.info("\r\n\r\n**************************** CHANGE TPIN *************************************");
		CommonResponse response = new CommonResponse();

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage("Invalid username");
				response.setRespCode("01");
				return response;
			}

			String oldPass = commonUtils.hashSHA256(req.getUsername().toUpperCase(), req.getOldTpin());
			if (!master.getTpin().equalsIgnoreCase(oldPass)) {
				response.setStatus(false);
				response.setMessage("Invalid Current TPIN");
				response.setRespCode("01");
				return response;
			}
			if (!req.getNewTpin().equalsIgnoreCase(req.getConfirmTpin())) {
				response.setStatus(false);
				response.setMessage("New TPIN and confirm TPIN must be the same.");
				response.setRespCode("01");
				return response;
			}
			String pass = commonUtils.hashSHA256(req.getUsername().toUpperCase(), req.getNewTpin());

			boolean passStatus = dbUtils.getLastThreeTpin(req.getUsername().toUpperCase(), pass);
			if (!passStatus) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.NEW_PASS_SHOUD_BE_DIFF_LAST_3_PASS);
				response.setRespCode("01");
				return response;
			}

			Date passExDt = dbUtils.getExpiryDt();

			PasswordHistory passhisty = new PasswordHistory();
			passhisty.setUserid(req.getUsername().toUpperCase());
			passhisty.setPassword_type("TPIN");
			passhisty.setOld_passwd(master.getPasswd());
			passhisty.setNew_passwd(pass);
			passhisty.setChangetime(new Date());
			passwordExpRepository.save(passhisty);

			master.setTpin(pass);
			master.setTpinExp(passExDt);
			masterRepository.save(master);

			response.setStatus(true);
			response.setMessage(ErrorMessages.TPIN_CHANGE_SUCCESS);
			response.setRespCode("00");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
