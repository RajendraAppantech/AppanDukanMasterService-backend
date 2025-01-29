package com.appan.usermanagement.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.usermanagement.models.ModifySettlementRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;

import jakarta.validation.Valid;

@Service
public class ModifySettlementService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private UserManagementMasterRepository userManagementMasterRepository;

    public CommonResponse updateStatus(@Valid ModifySettlementRequest req) {

        CommonResponse response = new CommonResponse();

        try {

            UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
            if (user == null) {
                response.setStatus(false);
                response.setMessage(ErrorMessages.INVALID_USERNAME);
                response.setRespCode("01");
                return response;
            }

            UserManagementMaster existingUser = userManagementMasterRepository.findById(req.getId()).orElse(null);
            if (existingUser == null) {
                response.setStatus(false);
                response.setMessage("User entry with the given ID not found.");
                response.setRespCode("02");
                return response;
            }

            existingUser.setStatus(req.getStatus());
            existingUser.setRemark(req.getRemark());
            existingUser.setModifyBy(req.getUsername());
            existingUser.setModifyDt(new Date());

            userManagementMasterRepository.save(existingUser);

            response.setStatus(true);
            response.setMessage("User data modified successfully.");
            response.setRespCode("00");
            return response;

        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("An error occurred while modifying the user entry.");
            response.setRespCode("03");
            return response;
        }
    }
}
