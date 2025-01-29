package com.appan.walletmaster.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.entity.WalletMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.WalletMasterRepository;
import com.appan.walletmaster.model.WalletModifyRequest;

@Service
public class WalletModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private WalletMasterRepository walletMasterRepository;

	public CommonResponse modifyWallet(WalletModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<WalletMaster> walletOptional = walletMasterRepository.findById(req.getId());
			if (!walletOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("Wallet with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			WalletMaster existingWallet = walletMasterRepository.findByWalletNameOrCode(req.getWalletName(),
					req.getCode());
			if (existingWallet != null && !existingWallet.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Wallet with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			WalletMaster wallet = walletOptional.get();
			wallet.setWalletName(req.getWalletName());
			wallet.setCode(req.getCode());
			wallet.setDescription(req.getDescription());
			wallet.setStatus(req.getStatus());
			wallet.setModifyBy(req.getUsername().toUpperCase());
			wallet.setModifyDt(new Date());

			walletMasterRepository.save(wallet);

			response.setStatus(true);
			response.setMessage("Wallet modified successfully.");
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
