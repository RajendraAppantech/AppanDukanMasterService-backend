package com.appan.tenant.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.TenantManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.TenantManagementMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.tenant.model.CreateTenantRequest;

import jakarta.validation.Valid;

@Service
public class CreateTenantService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TenantManagementMasterRepository tenantRepository;

	private static final Logger logger = LoggerFactory.getLogger(CreateTenantService.class);

	public CommonResponse create(@Valid CreateTenantRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			TenantManagementMaster existingTenant = tenantRepository
					.findByTenantNameIgnoreCase(req.getTenantName().trim());
			if (existingTenant != null) {
				response.setStatus(false);
				response.setMessage("Tenant entry already exists for the given tenant name.");
				response.setRespCode("01");
				return response;
			}

			TenantManagementMaster newTenant = new TenantManagementMaster();
			newTenant.setTenantName(req.getTenantName());
			newTenant.setDomainName(req.getDomainName());
			newTenant.setVersion(req.getVersion());
			newTenant.setSelectUserType(req.getSelectUserType());
			newTenant.setUserName(req.getUserName());
			newTenant.setTenantReference(req.getTenantReference());
			newTenant.setStatus(req.getStatus());
			newTenant.setCreatedBy(req.getUsername().toUpperCase());
			newTenant.setCreatedDt(new Date());
			newTenant.setAuthStatus("4");

			try {
				tenantRepository.save(newTenant);
			} catch (Exception e) {
				logger.error("Error while saving tenant entry: ", e);
				response.setStatus(false);
				response.setMessage("An error occurred while saving the tenant entry.");
				response.setRespCode("02");
				return response;
			}

			response.setStatus(true);
			response.setMessage("Tenant entry created successfully");
			response.setRespCode("00");
			return response;

		} catch (Exception e) {
			logger.error("Exception in create tenant service: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
