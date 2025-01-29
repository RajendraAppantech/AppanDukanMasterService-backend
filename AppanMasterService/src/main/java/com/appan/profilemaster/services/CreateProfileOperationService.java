package com.appan.profilemaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.ProfileOperationMaster; 
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.ProfileOperationMasterRepository;
import com.appan.profilemaster.model.CreateProfileOperationRequest;

import jakarta.validation.Valid;

@Service
public class CreateProfileOperationService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private ProfileOperationMasterRepository profileOperationMasterRepository;

    private static final Logger logger = LoggerFactory.getLogger(CreateProfileOperationService.class);

    public CommonResponse create(@Valid CreateProfileOperationRequest req) {
        CommonResponse response = new CommonResponse();

        try {
            UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
            if (user == null) {
                response.setStatus(false);
                response.setMessage(ErrorMessages.INVALID_USERNAME);
                response.setRespCode("01");
                return response;
            }

            ProfileOperationMaster existingProfileOperation = profileOperationMasterRepository
                    .findByOperationNameAndProfileName(
                            req.getOperationName().trim(),
                            req.getProfileName().trim()
                    );


            if (existingProfileOperation != null) {
                response.setStatus(false);
                response.setMessage("Profile operation with the same operation name and profile name already exists.");
                response.setRespCode("01");
                return response;
            }

            ProfileOperationMaster newProfileOperation = new ProfileOperationMaster();
            newProfileOperation.setOperationName(req.getOperationName().trim());
            newProfileOperation.setProfileName(req.getProfileName().trim());
            newProfileOperation.setStartDate(req.getStartDate());
            newProfileOperation.setEndDate(req.getEndDate());
            newProfileOperation.setIsDateValidity(req.getIsDateValidity());
            newProfileOperation.setIsOnlyUi(req.getIsOnlyUi());
            newProfileOperation.setStatus(req.getStatus());
            newProfileOperation.setCreatedBy(req.getUsername().toUpperCase());
            newProfileOperation.setCreatedDt(new Date());
            newProfileOperation.setAuthStatus("4");

            profileOperationMasterRepository.save(newProfileOperation);

            response.setStatus(true);
            response.setMessage("Profile operation created successfully.");
            response.setRespCode("00");
        } catch (Exception e) {
            logger.error("Exception occurred while creating profile operation: ", e);
            response.setStatus(false);
            response.setMessage("EXCEPTION");
            response.setRespCode("EX");
        }

        return response;
    }
}
