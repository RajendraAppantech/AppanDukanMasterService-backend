package com.appan.walletmaster.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.WalletMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.WalletMasterRepository;
import com.appan.walletmaster.model.WalletAuthOrBlockRequest;

import jakarta.validation.Valid;

@Service
public class WalletAuthOrBlockService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private WalletMasterRepository walletMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(WalletAuthOrBlockService.class);

	public CommonResponse authorBlockWallet(@Valid WalletAuthOrBlockRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			WalletMaster wallet = walletMasterRepository.findById(req.getId()).orElse(null);
			if (wallet == null) {
				response.setStatus(false);
				response.setMessage("Wallet with the given ID not found.");
				response.setRespCode("01");
				return response;
			}

			String currentStatus = wallet.getAuthStatus();
			if (req.getStatus().equalsIgnoreCase("1") && currentStatus.equalsIgnoreCase("1")) {
				response.setStatus(false);
				response.setMessage("Wallet is already authorized.");
				response.setRespCode("01");
				return response;
			}

			if (req.getStatus().equalsIgnoreCase("3") && currentStatus.equalsIgnoreCase("3")) {
				response.setStatus(false);
				response.setMessage("Wallet is already blocked.");
				response.setRespCode("01");
				return response;
			}

			wallet.setAuthBy(req.getUsername().toUpperCase());
			wallet.setAuthDate(new Date());

			if (req.getStatus().equalsIgnoreCase("1")) {
				wallet.setAuthStatus("1"); 
				response.setMessage("Wallet authorized successfully.");
			} else if (req.getStatus().equalsIgnoreCase("3")) {
				wallet.setAuthStatus("3"); 
				response.setMessage("Wallet blocked successfully.");
			} else {
				response.setStatus(false);
				response.setRespCode("02");
				response.setMessage("Invalid Status");
				return response;
			}

			walletMasterRepository.save(wallet);

			response.setStatus(true);
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
