package com.appan.userconfig.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.UsernameFormat;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UsernameFormatRepository;
import com.appan.userconfig.models.CreateUsernameFormateRequest;

import jakarta.validation.Valid;

@Service
public class CreateUsernameFormateService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private UsernameFormatRepository usernameFormatRepository;

    private static final Logger logger = LoggerFactory.getLogger(CreateUsernameFormateService.class);

    public CommonResponse create(@Valid CreateUsernameFormateRequest req) {
        CommonResponse response = new CommonResponse();

        try {
            UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
            if (user == null) {
                response.setStatus(false);
                response.setMessage(ErrorMessages.INVALID_USERNAME);
                response.setRespCode("01");
                return response;
            }

            UsernameFormat existingFormat = usernameFormatRepository.findByPrefix(req.getPrefix());
            if (existingFormat != null) {
                response.setStatus(false);
                response.setMessage("A username format entry with the same prefix already exists.");
                response.setRespCode("02");
                return response;
            }

            UsernameFormat newFormat = new UsernameFormat();
            newFormat.setUsernameFormat(req.getUsernameFormat());
            newFormat.setUserType(req.getUserType());
            newFormat.setPrefix(req.getPrefix());
            newFormat.setSuffix(req.getSuffix());
            newFormat.setStatus(req.getStatus());
            newFormat.setCreatedBy(req.getUsername().toUpperCase());
            newFormat.setCreatedDt(new Date());
            newFormat.setAuthStatus("4");

            try {
                usernameFormatRepository.save(newFormat);
            } catch (Exception e) {
                logger.error("Error while saving username format: ", e);
                response.setStatus(false);
                response.setMessage("An error occurred while saving the username format.");
                response.setRespCode("03");
                return response;
            }

            response.setStatus(true);
            response.setMessage("Username format created successfully.");
            response.setRespCode("00");
            return response;

        } catch (Exception e) {
            logger.error("Exception in create username format service: ", e);
            response.setStatus(false);
            response.setMessage("An unexpected error occurred.");
            response.setRespCode("EX");
            return response;
        }
    }
}
