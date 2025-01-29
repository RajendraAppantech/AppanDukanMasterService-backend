package com.appan.wallet.debitrequest.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.WalletDebitMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.wallet.debitrequest.model.ModifyWalletDebitRequest;
import com.appan.wallet.managepayment.repo.WalletRepositories.WalletDebitMasterRepository;

@Service
public class ModifyWalletDebitService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private WalletDebitMasterRepository walletDebitMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyWalletDebitService.class);

	public CommonResponse modify(ModifyWalletDebitRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			WalletDebitMaster walletDebit = walletDebitMasterRepository.findById(req.getId()).orElse(null);
			if (walletDebit == null) {
				response.setStatus(false);
				response.setMessage("Wallet debit entry with the given ID not found.");
				response.setRespCode("01");
				return response;
			}

			walletDebit.setStatus(req.getStatus());
			walletDebit.setUpdateRemark(req.getUpdateRemark());
			walletDebit.setModifyBy(req.getUsername().toUpperCase());
			walletDebit.setModifyDt(new Date());

			walletDebitMasterRepository.save(walletDebit);

			response.setStatus(true);
			response.setMessage("Wallet debit entry modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Error modifying wallet debit entry: ", e);
			response.setStatus(false);
			response.setMessage("An error occurred while modifying the wallet debit entry.");
			response.setRespCode("03");
			return response;
		}
	}
}
