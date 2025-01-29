package com.appan.tenant.services;

import java.util.Date;
import java.util.Optional;

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
import com.appan.tenant.model.ModifyTenantRequest;

import jakarta.validation.Valid;

@Service
public class ModifyTenantService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private TenantManagementMasterRepository tenantRepository;

	private static final Logger logger = LoggerFactory.getLogger(ModifyTenantService.class);

	public CommonResponse modify(@Valid ModifyTenantRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<TenantManagementMaster> tenantOptional = tenantRepository.findById(req.getId());
			if (!tenantOptional.isPresent()) {
				response.setStatus(false);
				response.setMessage("Tenant with the given ID not found.");
				response.setRespCode("01");
				return response;
			}

			TenantManagementMaster existingTenant = tenantRepository.findByTenantNameIgnoreCase(req.getTenantName());
			if (existingTenant != null && !existingTenant.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Tenant with the given name already exists.");
				response.setRespCode("02");
				return response;
			}

			TenantManagementMaster tenant = tenantOptional.get();
			tenant.setTenantName(req.getTenantName());
			tenant.setDomainName(req.getDomainName());
			tenant.setVersion(req.getVersion());
			tenant.setSelectUserType(req.getSelectUserType());
			tenant.setUserName(req.getUserName());
			tenant.setTenantReference(req.getTenantReference());
			tenant.setStatus(req.getStatus());
			tenant.setModifyBy(req.getUsername().toUpperCase());
			tenant.setModifyDt(new Date());

			tenantRepository.save(tenant);

			response.setStatus(true);
			response.setMessage("Tenant modified successfully.");
			response.setRespCode("00");

			return response;

		} catch (Exception e) {
			logger.error("EXCEPTION: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
