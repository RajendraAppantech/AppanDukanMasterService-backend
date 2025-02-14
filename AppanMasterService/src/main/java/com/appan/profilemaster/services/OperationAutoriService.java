package com.appan.profilemaster.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfilesMaster;
import com.appan.entity.RetailerUserMaster;
import com.appan.entity.UserMaster;
import com.appan.entity.UserMenu;
import com.appan.profilemaster.model.ModifyProfilesMasterRequest;
import com.appan.repositories.Repositories.ProfilesMasterRepository;
import com.appan.repositories.Repositories.RetailerUserMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;

import jakarta.validation.Valid;

@Service
public class OperationAutoriService {
	
	@Autowired
	private UserMenuRepository menuRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ProfilesMasterRepository profileMasterRepository;

	@Autowired
	private RetailerUserMasterRepository retailerUserMasterRepository;

	@Transactional
	public CommonResponse opAuth(@Valid ModifyProfilesMasterRequest req) {
	    CommonResponse response = new CommonResponse();

	    try {
	        // Check for authentication based on the username
	        UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
	        if (user == null) {
	            response.setStatus(false);
	            response.setMessage(ErrorMessages.INVALID_USERNAME);
	            response.setRespCode("01");
	            return response;
	        }

	        // Check if the profile exists by ID
	        Optional<ProfilesMaster> existingProfileOpt = profileMasterRepository.findById(req.getId());
	        if (!existingProfileOpt.isPresent()) {
	            response.setStatus(false);
	            response.setMessage("Profile with ID " + req.getId() + " does not exist.");
	            response.setRespCode("01");
	            return response;
	        }

	        ProfilesMaster existingProfile = existingProfileOpt.get();

	        // Update menu in ProfilesMaster
	        existingProfile.setMenu(req.getMenu());
	        profileMasterRepository.save(existingProfile);

	        // Update menu in UserMenu where user_profile matches profile_name (case-insensitive)
	        Optional<UserMenu> userMenuOpt = menuRepository.findByUserProfileIgnoreCase(req.getProfileName());
	        if (userMenuOpt.isPresent()) {
	            UserMenu userMenu = userMenuOpt.get();
	            userMenu.setMenu(req.getMenu());
	            menuRepository.save(userMenu);
	        }

	        // If userType is "Retailer", update retailer_user_master
            if ("Retailer".equalsIgnoreCase(existingProfile.getUserType())) {
                List<RetailerUserMaster> retailerUsers = retailerUserMasterRepository.findByUserProfileAndUserRole(req.getProfileName(), existingProfile.getUserType());
                for (RetailerUserMaster retailerUser : retailerUsers) {
                    retailerUser.setUserMenu(req.getMenu());
                    retailerUserMasterRepository.save(retailerUser);
                }
            } else {
                // If userType is not "Retailer", update user_menu in user_master
                List<UserMaster> users = userMasterRepository.findByUserProfileAndUserRole(req.getProfileName(), existingProfile.getUserType());
                for (UserMaster userEntry : users) {
                    userEntry.setUserMenu(req.getMenu());
                    userMasterRepository.save(userEntry);
                }
            }

	        response.setStatus(true);
	        response.setMessage("Profile, user menu, and retailer user menu updated successfully.");
	        response.setRespCode("00");
	        return response;

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setStatus(false);
	        response.setMessage("EXCEPTION");
	        response.setRespCode("EX");
	        return response;
	    }
	}
}