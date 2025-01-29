package com.appan.systemconfiguration.serivce;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.SystemConfigOperationType;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.systemconfiguration.models.SystemConfigOperationTypeModifyRequest;
import com.appan.systemconfiguration.repositories.SystemConfigurationRepositories.SystemConfigOperationTypeRepository;

@Service
public class SystemConfigOperationTypeModifyService {

    @Autowired
    private SystemConfigOperationTypeRepository repository;
    
    @Autowired
    private UserMasterRepository userMasterRepository;

    public CommonResponse modify(SystemConfigOperationTypeModifyRequest req) {
        CommonResponse response = new CommonResponse();
        try {
            
            UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
            if (user == null) {
                response.setStatus(false);
                response.setMessage(ErrorMessages.INVALID_USERNAME);
                response.setRespCode("01");
                return response;
            }
            
            SystemConfigOperationType existingEntry = repository.findById(req.getSystemConfigOperationTypeId()).orElse(null);
            if (existingEntry == null) {
                response.setStatus(false);
                response.setMessage("Operation Type id not found");
                response.setRespCode("01");
                return response;
            }

            SystemConfigOperationType existingMst = repository.findByOperationTypeIgnoreCase(req.getOperationType());
            if (existingMst != null && !existingMst.getSystemConfigOperationTypeId().equals(req.getSystemConfigOperationTypeId())) {
                response.setStatus(false);
                response.setMessage("OperationType name already exists.");
                response.setRespCode("02");
                return response;
            }

            existingEntry.setOperationType(req.getOperationType());
            existingEntry.setStatus(req.getStatus());
            existingEntry.setModifyBy(req.getUsername().toUpperCase());
            existingEntry.setModifyDt(new Date());
            repository.save(existingEntry);

            response.setStatus(true);
            response.setMessage("Operation Type modified successfully.");
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
