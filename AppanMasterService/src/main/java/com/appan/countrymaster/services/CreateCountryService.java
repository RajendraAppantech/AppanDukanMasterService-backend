package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.country.models.CreateCountryRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.CountryMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CountryMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

import jakarta.validation.Valid;

@Service
public class CreateCountryService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private CountryMasteRepository countryMasteRepository;

	public CommonResponse create(@Valid CreateCountryRequest req) {

		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}
			CountryMaster existingCountry = countryMasteRepository.findByCountryName(req.getCountryName().trim());

			if (existingCountry != null) {
				response.setStatus(false);
				response.setMessage("Country already exists");
				response.setRespCode("01");
				return response;
			}

			CountryMaster ms = countryMasteRepository.findTopByOrderByIdDesc();
			
			Long newId = ms != null ? ms.getId() + 1 : 1L;

			response = myUtils.saveImageToDisk(req.getFlag(), "flag.png", serverDocPath + "country/country/" + newId);
			if (!response.isStatus()) {
				return response;
			}

			String imgPath = doccumentsUrl + "?docPath=country/country&id=" + newId + "&fileName=flag.png";

			CountryMaster newCountry = new CountryMaster();
			newCountry.setCountryName(req.getCountryName());
			newCountry.setRegionName(req.getRegionName());
			newCountry.setRegex(req.getRegex());
			newCountry.setIsdCode(req.getIsdCode());
			newCountry.setNationality(req.getNationality());
			newCountry.setFlag(imgPath);
			newCountry.setStatus(req.getStatus());
			newCountry.setCreatedBy(req.getUsername().toUpperCase());
			newCountry.setCreatedDt(new Date());
			newCountry.setAuthStatus("4");

			try {
				countryMasteRepository.save(newCountry);
			} catch (Exception e) {
				response.setStatus(false);
				response.setMessage("An error occurred while saving the country.");
				response.setRespCode("02");
				return response;
			}

			response.setStatus(true);
			response.setMessage("Country created successfully");
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
