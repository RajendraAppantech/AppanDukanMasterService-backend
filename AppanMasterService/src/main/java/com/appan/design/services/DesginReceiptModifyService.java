package com.appan.design.services;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.design.models.DesginReceiptModifyRequest;
import com.appan.entity.DesignReceiptsMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.DesignReceiptsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;

@Service
public class DesginReceiptModifyService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private DesignReceiptsMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(DesginReceiptModifyService.class);

	public CommonResponse modify(DesginReceiptModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid username.");
				response.setRespCode("01");
				return response;
			}

			Optional<DesignReceiptsMaster> optionalMaster = repository.findById(req.getId());
			if (!optionalMaster.isPresent()) {
				response.setStatus(false);
				response.setMessage("Design Receipt with the given ID not found.");
				response.setRespCode("02");
				return response;
			}

			DesignReceiptsMaster master = optionalMaster.get();

			master.setDesign(req.getDesign());
			master.setCategory(req.getCategory());
			master.setParameter(req.getParameter());
			master.setStatus(req.getStatus());
			master.setReceiptTemplateBody(req.getReceiptTemplateBody());
			master.setModifyBy(req.getUsername().toUpperCase());
			master.setModifyDt(new Date());
			repository.save(master);

			response.setStatus(true);
			response.setMessage("Design Receipt modified successfully.");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Exception occurred while modifying Design Receipt: ", e);
			response.setStatus(false);
			response.setMessage("An exception occurred.");
			response.setRespCode("03");
			return response;
		}
	}
}
