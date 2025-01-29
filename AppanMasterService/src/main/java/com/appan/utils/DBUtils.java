package com.appan.utils;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class DBUtils {

//	@Autowired
//	private RentalCarMasterRepository rentalCarMasterRepository;

	@Autowired
	private UserMasterRepository masterRepository;

	private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int LENGTH = 6;

	public String generateAlphanumericString(int length) {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
			sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
		}

		return sb.toString();
	}

	public CommonResponse validateUserAndMobile(String userName, String mobileNo) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster master = masterRepository.findByUserId(userName.toUpperCase());
			if (master != null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.EXIST_USERNAME);
				response.setRespCode("01");
				return response;
			}

			if (!mobileNo.isEmpty()) {
				UserMaster master1 = masterRepository.findByMobileNo(mobileNo);
				if (master1 != null) {
					response.setStatus(false);
					response.setMessage(ErrorMessages.EXIST_MOBILENO);
					response.setRespCode("01");
					return response;
				}
			}
			response.setStatus(true);
			response.setMessage(ErrorMessages.SUCCESS);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
			return response;
		}
	}

	public String getCount(UserMaster master, String level) {
		try {
			if (level.equalsIgnoreCase("login")) {
				if ((master.getLoginAttempt() == 3 || master.getLoginAttempt() > 3)
						&& master.getLockTime().after(new Date())) {
					Date lockTime = master.getLockTime();
					Date time = new Date();
					long diff = lockTime.getTime() - time.getTime();
					long sec = diff / 1000;
					long min = sec / 60;
					return "for " + min + " Minutes";
				}

				master.setLoginAttempt(0);
				master.setLockTime(null);
				master.setLastLoginDt(new Date());
				masterRepository.save(master);
				return "true";
			}

			if ((master.getLoginAttempt() == 3 || master.getLoginAttempt() > 3)
					&& master.getLockTime().after(new Date())) {
				Date lockTime = master.getLockTime();
				Date time = new Date();
				long diff = lockTime.getTime() - time.getTime();
				long sec = diff / 1000;
				long min = sec / 60;
				return "for " + min + " Minutes";
			}

			if (master.getLoginAttempt() == 3 || master.getLoginAttempt() > 3) {
				return "false";
			}

			Date previous_time = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(previous_time);
			calendar.add(Calendar.HOUR, 1);
			previous_time = calendar.getTime();

			master.setLoginAttempt(master.getLoginAttempt() + 1);
			master.setLockTime(previous_time);
			masterRepository.save(master);
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}

	public Date getExpiryDt() {
		Date exDate;
		try {
			int expiryDays = 60;
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, expiryDays); // adds one days
			exDate = cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return exDate;
	}

/*	public String getNextRequestId() {
		String nextRequestId = "";
		try {
			// Fetch the last request ID from the database
			String lastRequestId = rentalCarMasterRepository.getLastRequestId();

			// If no previous ID exists, initialize the sequence
			if (lastRequestId == null) {
				lastRequestId = "00000";
			} else {
				// Extract the numeric part of the ID
				lastRequestId = lastRequestId.substring(2); // Skip the prefix (e.g., "AM")
			}

			// Increment the numeric part
			int nextId = Integer.parseInt(lastRequestId) + 1;

			// Format the new ID with the prefix and zero padding
			nextRequestId = "AM" + String.format("%05d", nextId);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return nextRequestId;
	}

	public static void main(String[] args) {

		// System.out.println("getId : "+getNextMid());
	}
	/*
	 * String abc = "swiiijsws"; char aa = 0; for (int i = 0; i < abc.length(); i++)
	 * { aa = abc.charAt(i); if(abc.indexOf(aa) == abc.lastIndexOf(aa)) { aa =
	 * abc.charAt(i); break; } } System.out.println("aa : " + aa); }
	 */

}
/*
 * String Swiss = "Swiss";
 * 
 * int cnt = 0;
 * 
 * for (int i = 0; i < Swiss.length(); i++) {
 * 
 * char c = Swiss.charAt(i);
 * 
 * cnt = 0;
 * 
 * for (int j = 0; j < Swiss.length(); j++) {
 * 
 * if (Swiss.charAt(j) == c) {
 * 
 * cnt++;
 * 
 * }
 * 
 * }
 * 
 * if (cnt == 1) {
 * 
 * System.out.println("Char " + c);
 * 
 * break; } }
 */