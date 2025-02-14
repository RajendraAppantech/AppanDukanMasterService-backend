package com.appan.usermenu.services;

import java.util.List;
import java.util.stream.Collectors;

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
import com.appan.usermenu.model.FetchUserRoleRequest;
import com.appan.usermenu.model.UserRoleDTO;

import jakarta.validation.Valid;

@Service
public class FetchUserRoleService {

	private static final Logger logger = LoggerFactory.getLogger(FetchUserRoleService.class);

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private UserMenuRepository menuRepository;

	public CommonResponse fetchuserrole(@Valid FetchUserRoleRequest req) {
		logger.info("**************************** FETCH USER ROLE *************************************");

		CommonResponse response = new CommonResponse();

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			//List<UserMenu> userMenuList = menuRepository.findByUserProfile(req.getUserProfile());
			List<UserMenu> userMenuList = menuRepository.findAllByUserProfile(req.getUserProfile());


			if (userMenuList.isEmpty()) {
				response.setStatus(false);
				response.setMessage("No roles found for this user profile.");
				response.setRespCode("02");
				return response;
			}

			List<UserRoleDTO> userRolesWithNames = userMenuList.stream()
					.map(menu -> new UserRoleDTO(menu.getUserRole(), menu.getRoleName())).collect(Collectors.toList());

			response.setStatus(true);
			response.setMessage("User roles fetched successfully");
			response.setRespCode("00");
			response.setData("userRolesWithNames", userRolesWithNames);

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
		}

		return response;
	}
}
