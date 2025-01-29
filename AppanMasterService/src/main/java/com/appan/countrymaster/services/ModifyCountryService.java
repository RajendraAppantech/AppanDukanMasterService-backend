package com.appan.countrymaster.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.country.models.ModifyCountryRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.CountryMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CountryMasteRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.utils.MyUtils;

@Service
public class ModifyCountryService {

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

	public CommonResponse modify(ModifyCountryRequest req) {
		CommonResponse response = new CommonResponse();

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			CountryMaster country = countryMasteRepository.findById(req.getId()).orElse(null);

			if (country == null) {
				response.setStatus(false);
				response.setMessage("Country with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			CountryMaster existingCountry = countryMasteRepository.findByCountryName(req.getCountryName());

			if (existingCountry != null && !existingCountry.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Country with the same name already exists.");
				response.setRespCode("03");
				return response;
			}

			if (req.getFlag() != null && !req.getFlag().isEmpty()) {
				
				if (!req.getFlag().startsWith("http") && !req.getFlag().startsWith("https")) {
					
					response = myUtils.saveImageToDisk(req.getFlag(), "flag.png", serverDocPath + "country/country/" + country.getId());
					
					
					if (!response.isStatus()) {
						return response;
					}

					String imgPath = doccumentsUrl + "?docPath=country/country&id=" + country.getId() + "&fileName=flag.png";
					country.setFlag(imgPath);
				}
			}

			country.setCountryName(req.getCountryName());
			country.setRegionName(req.getRegionName());
			country.setRegex(req.getRegex());
			country.setIsdCode(req.getIsdCode());
			country.setNationality(req.getNationality());
			country.setStatus(req.getStatus());
			country.setModifyBy(req.getUsername().toUpperCase());
			country.setModifyDt(new Date());

			countryMasteRepository.save(country);

			response.setStatus(true);
			response.setMessage("Country modified successfully.");
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
