package com.appan.bankmaster.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.bankmaster.entity.MasterIfsc;
import com.appan.bankmaster.masterifsc.model.MasterIfscModifyRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.MasterIfscRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class MasterIfscModifyService {

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

	public CommonResponse modifyMasterIfsc(@Valid MasterIfscModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			Optional<MasterIfsc> existingIfsc = masterIfscRepository.findById(req.getId());
			if (!existingIfsc.isPresent()) {
				response.setStatus(false);
				response.setMessage("Record with ID " + req.getId() + " does not exist.");
				response.setRespCode("01");
				return response;
			}

			if (req.getFile() != null && !req.getFile().isEmpty()) {
				
				if (!req.getFile().startsWith("http") && !req.getFile().startsWith("https")) {
				
				
				response = myUtils.saveImageToDisk(req.getFile(), "file.png",
						serverDocPath + "bank/masterifsc/" + req.getId());
				if (!response.isStatus()) {
					return response;
				}

				String filePath = doccumentsUrl + "?docPath=bank/masterifsc?id=" + req.getId() + "&fileName=file.png";
				existingIfsc.get().setFile(filePath);
			}

			}
			MasterIfsc masterIfsc = existingIfsc.get();
			masterIfsc.setBankName(req.getBankName());
			masterIfsc.setBankCode(req.getBankCode());
			masterIfsc.setIfscCode(req.getIfscCode());
			masterIfsc.setAeps(req.getAeps());
			masterIfsc.setDmtm(req.getDmtm());
			masterIfsc.setDmte(req.getDmte());
			masterIfsc.setDmtb(req.getDmtb());
			masterIfsc.setIsCreditCard(req.getIsCreditCard());
			masterIfsc.setPriority(req.getPriority());
			masterIfsc.setIsSettlement(req.getIsSettlement());
			masterIfsc.setModifyBy(req.getUsername().toUpperCase());
			masterIfsc.setModifyDt(new java.util.Date());

			masterIfscRepository.save(masterIfsc);

			response.setStatus(true);
			response.setMessage("Record updated successfully.");
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
