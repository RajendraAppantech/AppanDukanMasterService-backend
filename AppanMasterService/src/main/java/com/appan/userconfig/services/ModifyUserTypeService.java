package com.appan.userconfig.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UserMenu;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.userconfig.models.ModifyUserTypeRequest;

@Service
public class ModifyUserTypeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserMenuRepository userMenuRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyUserTypeService.class);

	public CommonResponse modify(ModifyUserTypeRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid username. User does not exist.");
				response.setRespCode("01");
				return response;
			}

			UserMenu userMenu = userMenuRepository.findById(req.getMenuId()).orElse(null);
			if (userMenu == null) {
				response.setStatus(false);
				response.setMessage("Invalid menuId. Menu does not exist.");
				response.setRespCode("02");
				return response;
			}

			UserMenu existingMenu = userMenuRepository.findByUserProfileAndUserRole(req.getUserProfile(),
					req.getUserRole());
			if (existingMenu != null && !existingMenu.getMenuId().equals(req.getMenuId())) {
				response.setStatus(false);
				response.setMessage("Duplicate entry. A user profile and role combination already exists.");
				response.setRespCode("03");
				return response;
			}

			userMenu.setRoleName(req.getRoleName());
			userMenu.setUserProfile(req.getUserProfile());
			userMenu.setUserRole(req.getUserRole());
			userMenu.setMenu(req.getMenu());
			userMenu.setRank(req.getRank());
			userMenu.setIsUser(req.getIsUser());
			userMenu.setIsAllow(req.getIsAllow());
			userMenu.setIsCommission(req.getIsCommission());
			userMenu.setStatus(req.getStatus());
			userMenu.setModifyBy(req.getUsername().toUpperCase());
			userMenu.setModifyDt(new Date());

			userMenuRepository.save(userMenu);

			response.setStatus(true);
			response.setMessage("User type modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Error modifying user type: ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while modifying the user type.");
			response.setRespCode("03");
			return response;
		}
	}
}
