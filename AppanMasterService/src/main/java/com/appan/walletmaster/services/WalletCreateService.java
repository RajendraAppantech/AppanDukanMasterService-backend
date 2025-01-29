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
import com.appan.walletmaster.model.WalletCreateRequest;

import jakarta.validation.Valid;

@Service
public class WalletCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private WalletMasterRepository walletMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(WalletCreateService.class);

	public CommonResponse createWallet(@Valid WalletCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			WalletMaster existingWallet = walletMasterRepository
					.findByWalletNameAndCodeIgnoreCase(req.getWalletName().trim(), req.getCode().trim());

			if (existingWallet != null) {
				response.setStatus(false);
				response.setMessage("Wallet with the same name and code already exists.");
				response.setRespCode("01");
				return response;
			}

			WalletMaster newWallet = new WalletMaster();
			newWallet.setWalletName(req.getWalletName().trim());
			newWallet.setCode(req.getCode().trim());
			newWallet.setDescription(req.getDescription().trim());
			newWallet.setStatus(req.getStatus());
			newWallet.setCreatedBy(req.getUsername().toUpperCase());
			newWallet.setCreatedDt(new Date());
			newWallet.setAuthStatus("4");

			walletMasterRepository.save(newWallet);

			response.setStatus(true);
			response.setMessage("Wallet created successfully.");
			response.setRespCode("00");
		} catch (Exception e) {
			logger.error("Exception occurred while creating wallet: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
		}

		return response;
	}
}
