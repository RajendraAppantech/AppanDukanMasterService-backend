package com.appan.design.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.design.models.DesginReceiptAuthOrBlockRequest;
import com.appan.entity.DesignReceiptsMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.DesignReceiptsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

import jakarta.validation.Valid;

@Service
public class DesginReceiptAuthOrBlockService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private DesignReceiptsMasterRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(DesginReceiptAuthOrBlockService.class);

    public CommonResponse authorblock(@Valid DesginReceiptAuthOrBlockRequest req) {
        CommonResponse response = new CommonResponse();

        try {
            UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
            if (user == null) {
                response.setStatus(false);
                response.setMessage("Invalid username.");
                response.setRespCode("01");
                return response;
            }

            DesignReceiptsMaster master = repository.findById(req.getId()).orElse(null);
            if (master == null) {
                response.setStatus(false);
                response.setMessage("Design Receipt with the given ID not found.");
                response.setRespCode("02");
                return response;
            }

            String currentStatus = master.getAuthStatus();
            if (req.getStatus().equalsIgnoreCase("1") && currentStatus.equalsIgnoreCase("1")) {
                response.setStatus(false);
                response.setMessage("Design Receipt is already authorized.");
                response.setRespCode("03");
                return response;
            }

            if (req.getStatus().equalsIgnoreCase("3") && currentStatus.equalsIgnoreCase("3")) {
                response.setStatus(false);
                response.setMessage("Design Receipt is already blocked.");
                response.setRespCode("04");
                return response;
            }

            master.setAuthBy(req.getUsername().toUpperCase());
            master.setAuthDate(new Date());

            if (req.getStatus().equalsIgnoreCase("1")) {
                master.setAuthStatus("1");
                response.setMessage("Design Receipt authorized successfully.");
            } else if (req.getStatus().equalsIgnoreCase("3")) {
                master.setAuthStatus("3");
                response.setMessage("Design Receipt blocked successfully.");
            } else {
                response.setStatus(false);
                response.setRespCode("05");
                response.setMessage("Invalid Status.");
                return response;
            }

            repository.save(master);
            response.setStatus(true);
            response.setRespCode("00");
            return response;

        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            response.setStatus(false);
            response.setMessage("An exception occurred.");
            response.setRespCode("EX");
            return response;
        }
    }
}
