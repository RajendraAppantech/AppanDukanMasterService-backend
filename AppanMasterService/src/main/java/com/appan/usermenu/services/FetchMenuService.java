package com.appan.usermenu.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.usermenu.model.FetchmenuRequest;
import com.google.gson.GsonBuilder;

import jakarta.validation.Valid;

@Service
public class FetchMenuService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private UserMenuRepository menuRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchMenuService.class);

	public CommonResponse fetchmenu(@Valid FetchmenuRequest req) {

		logger.info("\r\n\r\n**************************** FETCH MENU *************************************");

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

			// Step 2: Check if user_id is provided; fetch data accordingly
			if (req.getUserId() != null && !req.getUserId().isEmpty()) {
				// Fetch user_menu from UserMaster
				String userMenu = masterRepository.findUserMenuByUserIdRoleAndProfile(req.getUserId().toUpperCase(),
						req.getUserRole().toUpperCase(), req.getUserProfile().toUpperCase());

				if (userMenu != null) {
					response.setStatus(true);
					response.setMessage("User menu fetched successfully");
					response.setRespCode("00");
					response.setData("userMenu", userMenu);
				} else {
					response.setStatus(false);
					response.setMessage("No matching user menu found");
					response.setRespCode("02");
				}
			} else {
				// Fetch menu from UserMenu
				List<String> menuList = menuRepository.findMenuByRoleAndProfile(req.getUserRole().toUpperCase(),
						req.getUserProfile().toUpperCase());

				if (!menuList.isEmpty()) {
					response.setStatus(true);
					response.setMessage("Menus fetched successfully");
					response.setRespCode("00");

					if (menuList.size() == 1) {
						response.setData("menuList", menuList.get(0));
					} else {
						response.setData("menuList", menuList);
					}
				} else {
					response.setStatus(false);
					response.setMessage("No matching menus found");
					response.setRespCode("02");
				}
			}

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
		}

		return response;
	}
}
