package com.appan.utils;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;

@Service
public class CommonUtils {

	public String hashSHA256(String username, String password) {
		return Hashing.sha256().hashString(username + password + "smsgateway", StandardCharsets.UTF_8).toString();
	}
	
	public static String hashSHA256static(String username, String password) {
		return Hashing.sha256().hashString(username + password + "smsgateway", StandardCharsets.UTF_8).toString();
	}

	public Date getStartDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date startOfDay = calendar.getTime();

		return startOfDay;
	}

	public Date getEndDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date endOfDay = calendar.getTime();
		return endOfDay;
	}
	
	public static void main(String[] args) {
			String pass = hashSHA256static("VINAYAK01" , "1234");
			System.out.println("pass : "+pass);
	}
}