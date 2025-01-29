package com.appan.serviceConfig.category.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ServiceConfigCategoryMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.category.model.CategoryModifyRequest;
import com.appan.utils.MyUtils;

@Service
public class CategoryModifyService {

	@Value("${documents_url}")
	private String doccumentsUrl;

	@Value("${server_doc_path}")
	private String serverDocPath;

	@Autowired
	private MyUtils myUtils;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigCategoryRepository repository;

	public CommonResponse modify(CategoryModifyRequest req) {
		CommonResponse response = new CommonResponse();

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage(ErrorMessages.INVALID_USERNAME);
				response.setRespCode("01");
				return response;
			}

			Optional<ServiceConfigCategoryMaster> msts = repository.findById(req.getId());
			if (!msts.isPresent()) {
				response.setStatus(false);
				response.setMessage("Service Category with the given ID not found");
				response.setRespCode("01");
				return response;
			}

			ServiceConfigCategoryMaster existingServiceConfigCategoryMaster = repository
					.findByCategoryNameAndCategoryCode(req.getCategoryName(), req.getCategoryCode());
			if (existingServiceConfigCategoryMaster != null
					&& !existingServiceConfigCategoryMaster.getId().equals(req.getId())) {
				response.setStatus(false);
				response.setMessage("Service Category with the given name or code already exists");
				response.setRespCode("02");
				return response;
			}

			ServiceConfigCategoryMaster mst = msts.get();

			if (req.getImage() != null && !req.getImage().isEmpty()) {

				if (!req.getImage().startsWith("http") && !req.getImage().startsWith("https")) {
					response = myUtils.saveImageToDisk(req.getImage(), "image.png",	serverDocPath + "serviceconfig/category/" + mst.getId());
					if (!response.isStatus()) {
						return response;
					}

					String imgPath = doccumentsUrl + "?docPath=serviceconfig/category?id=" + mst.getId() + "&fileName=image.png";
					mst.setImage(imgPath);
				}
			}

			mst.setCategoryName(req.getCategoryName());
			mst.setCategoryCode(req.getCategoryCode());
			mst.setIsTpin(req.getIsTpin());
			mst.setStatus(req.getStatus());
			mst.setModifyBy(req.getUsername().toUpperCase());
			mst.setModifyDt(new Date());
			repository.save(mst);

			response.setStatus(true);
			response.setMessage("Service Category modified successfully.");
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
