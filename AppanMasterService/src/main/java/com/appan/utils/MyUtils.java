package com.appan.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.PasswordHistory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.PasswordExpRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Component
public class MyUtils {

	private static final Logger logger = LoggerFactory.getLogger(MyUtils.class);
	
	@Autowired
	private UserMasterRepository masterRepository;
	
	@Autowired
	private PasswordExpRepository passwordExpRepository;

	public boolean getLastThreePassword(String username, String newpass) {
		try {
			List<PasswordHistory> lstpassword = passwordExpRepository.fetchPasswordAndOldPasswd(username , newpass);
			System.out.println("lstpassword.size() : "+lstpassword.size());
			if(lstpassword.size() >= 3) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean getLastThreeTpin(String username, String newpass) {
		try {
			List<PasswordHistory> lstpassword = passwordExpRepository.fetchTpinAndOldTpin(username , newpass , "TPIN");
			System.out.println("lstpassword.size() : "+lstpassword.size());
			if(lstpassword.size() >= 3) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public CommonResponse saveImageToDisk(String attachment, String imageName, String storageDirectoryPath) {
		CommonResponse response = new CommonResponse();
		try {
			// Check if the string contains the Base64 prefix and strip it if present
			if (attachment.startsWith("data:image/")) {
				int commaIndex = attachment.indexOf(',');
				if (commaIndex != -1) {
					attachment = attachment.substring(commaIndex + 1); // Remove the prefix
				}
			}

			// Generate a unique image name
			// tring imageName = "wallet_load_" + System.currentTimeMillis() + ".png"; //
			// Unique name based on current
			// timestamp
			String filePath = storageDirectoryPath + "/" + imageName; // File path to store the image

			// Ensure the storage directory exists
			File storageDir = new File(storageDirectoryPath);
			if (!storageDir.exists() && !storageDir.mkdirs()) {
				response.setStatus(false);
				response.setMessage("Failed to create storage directory: " + storageDirectoryPath);
				response.setRespCode("01");
				return response;
			}

			// Decode the Base64 string to get the image bytes
			byte[] imageBytes = Base64.getDecoder().decode(attachment);

			// Optional: Validate file size (e.g., max size 1MB)
			if (imageBytes.length > 1024 * 1024) {
				response.setStatus(false);
				response.setMessage("Imgae size is greater than 1MB, Please resize the images and try again.");
				response.setRespCode("01");
				return response;
			}

			// Validate image format using the MIME type (PNG/JPEG)
			ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
			BufferedImage image = ImageIO.read(bis);
			if (image == null) {
				response.setStatus(false);
				response.setMessage("Invalid image format.");
				response.setRespCode("01");
				return response;
			}

			// Write the bytes to a file
			File imageFile = new File(filePath);
			try (FileOutputStream fos = new FileOutputStream(imageFile)) {
				fos.write(imageBytes);
			}

			logger.info("Image saved successfully at: " + filePath);
			response.setStatus(true);
			response.setMessage("Image saved successfully at: " + filePath);
			response.setRespCode("00");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("EXCEPTION " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("01");
			return response;
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
}
