package com.appan.usermanagement.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.BackOfficeManagement;
import com.appan.entity.UserMaster;
import com.appan.usermanagement.models.CreateBackOfficeRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.BackOfficeManagementRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UsernameFormatRepository;

import jakarta.validation.Valid;

@Service
public class BackOfficeCreateService {

    @Autowired
    private BackOfficeManagementRepository repository;

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private UsernameFormatRepository usernameFormatRepository;

    public CommonResponse create(@Valid CreateBackOfficeRequest req) {
        CommonResponse response = new CommonResponse();

        try {
            UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
            if (user == null) {
                response.setStatus(false);
                response.setMessage("Invalid username");
                response.setRespCode("01");
                return response;
            }

            String prefix = usernameFormatRepository.findPrefixByUserType(req.getUserType());
            if (prefix == null) {
                response.setStatus(false);
                response.setMessage("Invalid user type, prefix not found.");
                response.setRespCode("02");
                return response;
            }

            String customUserId = generateCustomUserId(req.getUserType(), prefix);

            BackOfficeManagement backOffice = new BackOfficeManagement();
            backOffice.setUserId(customUserId);
            backOffice.setUserType(req.getUserType());
            backOffice.setName(req.getName());
            backOffice.setMobile(req.getMobile());
            backOffice.setEmail(req.getEmail());
            backOffice.setAlternateMobile(req.getAlternateMobile());
            backOffice.setCompanyName(req.getCompanyName());
            backOffice.setStatus("Active");
            backOffice.setCreatedBy(req.getUsername().toUpperCase());
            backOffice.setCreatedDt(new Date());
            backOffice.setAuthStatus("4");

            repository.save(backOffice);

            response.setStatus(true);
            response.setMessage("Back office user created successfully.");
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

    private String generateCustomUserId(String userType, String prefix) {

		if (prefix == null) {
			throw new IllegalArgumentException("Prefix cannot be null.");
		}

		if (userType == null) {
			throw new IllegalArgumentException("User type cannot be null.");
		}

		String lastUserId = repository.findLastUserIdByUserType(userType);

		String numberPart = "000000";
		if (lastUserId != null && lastUserId.length() > 6) {
			numberPart = lastUserId.substring(lastUserId.length() - 6);
		}

		int currentNumber = Integer.parseInt(numberPart) + 1;
		String newNumberPart = String.format("%06d", currentNumber);

		return prefix + newNumberPart;
	}
}
