package com.appan.bankmaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.entity.MasterIfsc;
import com.appan.bankmaster.masterifsc.model.MasterIfscCreateRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.MasterIfscRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class MasterIfscCreateService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private MasterIfscRepository masterIfscRepository;

	public CommonResponse createMasterIfsc(@Valid MasterIfscCreateRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			MasterIfsc existingIfsc = masterIfscRepository.findByIfscCode(req.getIfscCode().trim());

			if (existingIfsc != null) {
				response.setStatus(false);
				response.setMessage("IFSC Code already exists");
				response.setRespCode("01");
				return response;
			}

			MasterIfsc ms = masterIfscRepository.findTopByOrderByIdDesc();

			Long newId = ms.getId() + 1;

			response = myUtils.saveImageToDisk(req.getFile(), "file.png", serverDocPath + "bank/masterifsc/" + newId);
			if (!response.isStatus()) {
				return response;
			}

			String imgPath = doccumentsUrl + "?docPath=bank/masterifsc?id=" + newId + "&fileName=file.png";

			MasterIfsc newMasterIfsc = new MasterIfsc();
			newMasterIfsc.setBankName(req.getBankName());
			newMasterIfsc.setBankCode(req.getBankCode());
			newMasterIfsc.setIfscCode(req.getIfscCode());
			newMasterIfsc.setAeps(req.getAeps());
			newMasterIfsc.setDmtm(req.getDmtm());
			newMasterIfsc.setDmte(req.getDmte());
			newMasterIfsc.setDmtb(req.getDmtb());
			newMasterIfsc.setIsCreditCard(req.getIsCreditCard());
			newMasterIfsc.setPriority(req.getPriority());
			newMasterIfsc.setIsSettlement(req.getIsSettlement());
			newMasterIfsc.setFile(imgPath);
			newMasterIfsc.setCreatedBy(req.getUsername().toUpperCase());
			newMasterIfsc.setCreatedDt(new Date());
			newMasterIfsc.setAuthStatus("4");

			try {
				masterIfscRepository.save(newMasterIfsc);
			} catch (Exception e) {
				response.setStatus(false);
				response.setMessage("An error occurred while saving the IFSC data.");
				response.setRespCode("02");
				return response;
			}

			response.setStatus(true);
			response.setMessage("IFSC Code created successfully");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
