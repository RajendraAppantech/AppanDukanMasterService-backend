package com.appan.apimaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.apimaster.models.TransactionApiModifyRequest;
import com.appan.apimaster.repositories.ApiRepositories.TransactionApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TransactionApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionApiModifyService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private TransactionApiMasterRepository transactionApiMasterRepository;

    public CommonResponse modifyTransactionApi(TransactionApiModifyRequest req) {
        CommonResponse response = new CommonResponse();

        try {
            UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
            if (user == null) {
                response.setStatus(false);
                response.setMessage(ErrorMessages.INVALID_USERNAME);
                response.setRespCode("01");
                return response;
            }

            Optional<TransactionApiMaster> existingApi = transactionApiMasterRepository.findById(req.getId());
            if (!existingApi.isPresent()) {
                response.setStatus(false);
                response.setMessage("Transaction API with the given ID not found");
                response.setRespCode("01");
                return response;
            }

            TransactionApiMaster apiMaster = existingApi.get();
            TransactionApiMaster existingApiData = transactionApiMasterRepository
                    .findByApiNameAndCodeAndType(req.getApiName(), req.getCode(), req.getType());

            if (existingApiData != null && !existingApiData.getId().equals(req.getId())) {
                response.setStatus(false);
                response.setMessage("API with the given name, code, and type already exists");
                response.setRespCode("02");
                return response;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String parametersTypeAsString = objectMapper.writeValueAsString(req.getParametersType());

            apiMaster.setApiName(req.getApiName());
            apiMaster.setBalance(req.getBalance());
            apiMaster.setCode(req.getCode());
            apiMaster.setIsActive(req.getIsActive());
            apiMaster.setIsUserwise(req.getIsUserwise());
            apiMaster.setType(req.getType());
            apiMaster.setStatus(req.getStatus());
            apiMaster.setParametersType(parametersTypeAsString);
            apiMaster.setModifyBy(req.getUsername().toUpperCase());
            apiMaster.setModifyDt(new Date());

            transactionApiMasterRepository.save(apiMaster);

            response.setStatus(true);
            response.setMessage("Transaction API modified successfully.");
            response.setRespCode("00");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("EXCEPTION");
            response.setRespCode("03");
            return response;
        }
    }
}
