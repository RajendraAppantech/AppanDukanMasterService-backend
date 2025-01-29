package com.appan.usermenu.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UserMenu;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.usermenu.model.ModifyUsermenuRequest;

import jakarta.validation.Valid;

@Service
public class UsermenuModifyService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private UserMenuRepository menuRepository;

	private static final Logger logger = LoggerFactory.getLogger(UsermenuModifyService.class);

	public CommonResponse modifyUsermenu(@Valid ModifyUsermenuRequest req) {

		logger.info("\r\n\r\n**************************** MODIFY USERMENU *************************************");
		CommonResponse response = new CommonResponse();

		try {
			// Step 1: Validate user existence for authorization
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			// Step 2: Find the specific UserMenu entry by menu_id
			UserMenu existingUserMenu = menuRepository.findById(req.getMenuId()).orElse(null);
			if (existingUserMenu == null) {
				response.setStatus(false);
				response.setMessage("User menu entry not found for the provided menu_id.");
				response.setRespCode("02");
				return response;
			}

			// Step 3: Check for duplicate UserMenu entries with the same userProfile and
			// userRole
			UserMenu duplicateUserMenu = menuRepository.findByUserProfileAndUserRoleAndMenuIdNot(
					req.getUserProfile().toUpperCase(), req.getUserRole().toUpperCase(), req.getMenuId());

			if (duplicateUserMenu != null) {
				response.setStatus(false);
				response.setMessage("Another entry with the same user profile and role already exists.");
				response.setRespCode("03");
				return response;
			}

			// Step 4: Check if there is an attempt to modify the userRole
			if (!existingUserMenu.getUserRole().equalsIgnoreCase(req.getUserRole())) {
				response.setStatus(false);
				response.setMessage("Modification of userRole is not allowed.");
				response.setRespCode("04");
				return response;
			}

			// Step 5: Modify the existing UserMenu entry with new data
			existingUserMenu.setUserProfile(req.getUserProfile());
			existingUserMenu.setUserType(req.getUserType());
			existingUserMenu.setCode(req.getCode());
			existingUserMenu.setMenu(req.getMenu());
			existingUserMenu.setModifyBy(req.getUsername().toUpperCase());
			existingUserMenu.setModifyDt(new Date());
			existingUserMenu.setRoleName(req.getRoleName());

			// Save the updated UserMenu entry
			menuRepository.save(existingUserMenu);

			// Success response
			response.setStatus(true);
			response.setMessage("User menu modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
		}

		return response;
	}
}
