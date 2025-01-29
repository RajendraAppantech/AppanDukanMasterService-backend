package com.appan.apimaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.apimaster.models.TransactionApiCreateRequest;
import com.appan.apimaster.repositories.ApiRepositories.TransactionApiMasterRepository;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TransactionApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.validation.Valid;

@Service
public class TransactionApiCreateService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TransactionApiMasterRepository transactionApiMasterRepository;

	public CommonResponse createTransactionApi(@Valid TransactionApiCreateRequest req) {
		CommonResponse response = new CommonResponse();

		 try {
		        UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
		        if (user == null) {
		            response.setStatus(false);
		            response.setMessage("Invalid username.");
		            response.setRespCode("01");
		            return response;
		        }

		        TransactionApiMaster existingApi = transactionApiMasterRepository.findByApiNameAndCode(req.getApiName(),
		                req.getCode());
		        if (existingApi != null) {
		            response.setStatus(false);
		            response.setMessage("API with this name and code already exists.");
		            response.setRespCode("02");
		            return response;
		        }

		        ObjectMapper objectMapper = new ObjectMapper();
		        String parametersTypeAsString = objectMapper.writeValueAsString(req.getParametersType());

		        TransactionApiMaster newApi = new TransactionApiMaster();
		        newApi.setApiName(req.getApiName());
		        newApi.setBalance(req.getBalance());
		        newApi.setCode(req.getCode());
		        newApi.setIsActive(req.getIsActive());
		        newApi.setIsUserwise(req.getIsUserwise());
		        newApi.setType(req.getType());
		        newApi.setStatus(req.getStatus());
		        newApi.setParametersType(parametersTypeAsString); 
		        newApi.setCreatedDt(new Date());
		        newApi.setCreatedBy(req.getUsername().toUpperCase());
		        newApi.setAuthStatus("4");

		        transactionApiMasterRepository.save(newApi);

		        response.setStatus(true);
		        response.setMessage("Transaction API created successfully.");
		        response.setRespCode("00");

		    } catch (Exception e) {
		        e.printStackTrace();
		        response.setStatus(false);
		        response.setMessage("EXCEPTION");
		        response.setRespCode("EX");
		        return response;
		    }

		    return response;
		}

	
}
