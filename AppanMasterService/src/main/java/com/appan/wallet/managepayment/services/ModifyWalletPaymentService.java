package com.appan.wallet.managepayment.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.wallet.managepayment.model.ModifyWalletPaymentRequest;
import com.appan.wallet.managepayment.repo.WalletRepositories.WalletPaymentMasterRepository;
import com.appan.entity.WalletPaymentMaster;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class ModifyWalletPaymentService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private WalletPaymentMasterRepository walletPaymentMasterRepository;

    private static final Logger logger = LoggerFactory.getLogger(ModifyWalletPaymentService.class);

    public CommonResponse modify(ModifyWalletPaymentRequest req) {
        CommonResponse response = new CommonResponse();

        try {
            UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
            if (user == null) {
                response.setStatus(false);
                response.setMessage(ErrorMessages.INVALID_USERNAME);
                response.setRespCode("01");
                return response;
            }

            WalletPaymentMaster walletPayment = walletPaymentMasterRepository.findById(req.getId()).orElse(null);
            if (walletPayment == null) {
                response.setStatus(false);
                response.setMessage("Wallet payment entry with the given ID not found.");
                response.setRespCode("01");
                return response;
            }

            walletPayment.setStatus(req.getStatus());
            walletPayment.setUpdateRemark(req.getUpdateRemark());
            walletPayment.setModifyBy(req.getUsername().toUpperCase());
            walletPayment.setModifyDt(new Date());

            walletPaymentMasterRepository.save(walletPayment);

            response.setStatus(true);
            response.setMessage("Wallet payment entry modified successfully.");
            response.setRespCode("00");
            return response;

        } catch (Exception e) {
            logger.error("Error modifying wallet payment entry: ", e);
            response.setStatus(false);
            response.setMessage("An error occurred while modifying the wallet payment entry.");
            response.setRespCode("03");
            return response;
        }
    }
}
