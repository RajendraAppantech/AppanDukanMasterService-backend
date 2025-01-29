package com.appan.usermenu.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.usermenu.model.UpdateUserMenuReq;

@Service
public class UpdateUserMenuService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private UserMenuRepository menuRepository;

	private static final Logger logger = LoggerFactory.getLogger(UpdateUserMenuService.class);

	@Transactional
	public CommonResponse updateUserMenu(UpdateUserMenuReq req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster userByUsername = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (userByUsername == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			if (req.getUserId() != null && !req.getUserId().isEmpty()) {
				UserMaster master = masterRepository.findByUserId(req.getUserId().toUpperCase());
				if (master == null) {
					response.setStatus(false);
					response.setMessage(ErrorMessages.NOT_FOUND);
					response.setRespCode("01");
					return response;
				}

				if (!master.getUserProfile().equalsIgnoreCase(req.getUserProfile())) {
					response.setStatus(false);
					response.setMessage(ErrorMessages.NOT_FOUND);
					response.setRespCode("03");
					return response;
				}

				master.setUserMenu(req.getUserMenu());
				master.setModifyBy(req.getUsername().toUpperCase());
				master.setModifyDt(new Date());
				masterRepository.save(master);

				response.setStatus(true);
				response.setMessage("User menu " + ErrorMessages.MODIFY_SUCCESS);
				response.setRespCode("00");
			} else {
				int updatedRows = menuRepository.updateMenuByRoleAndProfile(req.getUserRole().toUpperCase(),
						req.getUserProfile().toUpperCase(), req.getUserMenu());

				if (updatedRows > 0) {
					response.setStatus(true);
					response.setMessage("Menu updated successfully");
					response.setRespCode("00");
				} else {
					response.setStatus(false);
					response.setMessage("No menu update made possibly no matching record");
					response.setRespCode("02");
				}
			}
		} catch (Exception e) {
			logger.error("Exception occurred while updating user menu: ", e);
			response.setStatus(false);
			response.setMessage(ErrorMessages.EXCEPTION);
			response.setRespCode("EX");
		}

		return response;
	}
}
