package com.appan.usermanagement.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.entity.ProfilesMaster;
import com.appan.entity.UserManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ProfilesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.usermanagement.models.CommonKycResponse;
import com.appan.usermanagement.models.KycStatusCount;
import com.appan.usermanagement.models.KycSummaryRequst;
import com.appan.usermanagement.models.UpdateCapBalRequst;
import com.appan.usermanagement.models.UpdateEmailIdRequest;
import com.appan.usermanagement.models.UpdateMobileNoRequest;
import com.appan.usermanagement.models.UpdateProfileRequest;
import com.appan.usermanagement.models.UpdateUserStatusRequest;
import com.appan.usermanagement.models.UpdateWalletStatusRequest;
import com.appan.usermanagement.models.UserManagementFetchRequest;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;
import com.appan.utils.CommonUtils;

import jakarta.validation.Valid;

@Service
public class UpdateBasicDetailsService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserManagementMasterRepository repository;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private ProfilesMasterRepository profilesMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(UpdateBasicDetailsService.class);

	public CommonResponse updateMobileNo(UpdateMobileNoRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			UserManagementMaster mast = repository.findByUserId(req.getUserId());
			if (mast == null) {
				response.setStatus(false);
				response.setMessage("User not found");
				response.setRespCode("01");
				return response;
			}

			List<UserManagementMaster> masMob = repository.findByMobileNumber(req.getMobileNo());
			if (!masMob.isEmpty()) {
				response.setStatus(false);
				response.setMessage("Mobile no already register with another user");
				response.setRespCode("01");
				return response;
			}

			String checkTpin = commonUtils.hashSHA256(req.getUsername().toUpperCase(), req.getTpin());
			if (!user.getTpin().equalsIgnoreCase(checkTpin)) {
				response.setStatus(false);
				response.setMessage("Invalid TPIN");
				response.setRespCode("01");
				return response;
			}

			mast.setMobileNumber(req.getMobileNo());
			mast.setIsMobileVerified(true);
			mast.setModifyBy(req.getUsername().toUpperCase());
			mast.setModifyDt(new Date());
			repository.save(mast);

			response.setStatus(true);
			response.setRespCode("00");
			response.setMessage("Mobile no updated successfully");
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

	public CommonResponse updateEmailId(UpdateEmailIdRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			UserManagementMaster mast = repository.findByUserId(req.getUserId());
			if (mast == null) {
				response.setStatus(false);
				response.setMessage("User not found");
				response.setRespCode("01");
				return response;
			}

			List<UserManagementMaster> masMob = repository.findByEmailId(req.getEmailId());
			if (!masMob.isEmpty()) {
				response.setStatus(false);
				response.setMessage("Email ID already register with another user");
				response.setRespCode("01");
				return response;
			}

			String checkTpin = commonUtils.hashSHA256(req.getUsername().toUpperCase(), req.getTpin());
			if (!user.getTpin().equalsIgnoreCase(checkTpin)) {
				response.setStatus(false);
				response.setMessage("Invalid TPIN");
				response.setRespCode("01");
				return response;
			}

			mast.setEmailId(req.getEmailId());
			mast.setIsEmailVerified(true);
			mast.setModifyBy(req.getUsername().toUpperCase());
			mast.setModifyDt(new Date());
			repository.save(mast);

			response.setStatus(true);
			response.setRespCode("00");
			response.setMessage("Email ID updated successfully");
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

	public CommonResponse updateProfile(UpdateProfileRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			UserManagementMaster mast = repository.findByUserId(req.getUserId());
			if (mast == null) {
				response.setStatus(false);
				response.setMessage("User not found");
				response.setRespCode("01");
				return response;
			}

			List<ProfilesMaster> prMst = profilesMasterRepository.findByProfileName(req.getProfile());
			if (prMst.isEmpty()) {
				response.setStatus(false);
				response.setMessage("Profile name not found");
				response.setRespCode("01");
				return response;
			}

			mast.setProfile(req.getProfile());
			mast.setModifyBy(req.getUsername().toUpperCase());
			mast.setModifyDt(new Date());
			repository.save(mast);

			response.setStatus(true);
			response.setRespCode("00");
			response.setMessage("Profile updated successfully");
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

	public CommonResponse updateWalletStatus(UpdateWalletStatusRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			UserManagementMaster mast = repository.findByUserId(req.getUserId());
			if (mast == null) {
				response.setStatus(false);
				response.setMessage("User not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getWalletStatus().equalsIgnoreCase("Unfreeze")
					&& mast.getWalletStatus().equalsIgnoreCase("Unfreeze")) {
				response.setStatus(false);
				response.setMessage("Wallet status is already Unfreeze");
				response.setRespCode("01");
				return response;
			}

			if (req.getWalletStatus().equalsIgnoreCase("freeze") && mast.getWalletStatus().equalsIgnoreCase("freeze")) {
				response.setStatus(false);
				response.setMessage("Wallet status is already freeze");
				response.setRespCode("01");
				return response;
			}

			String checkTpin = commonUtils.hashSHA256(req.getUsername().toUpperCase(), req.getTpin());
			if (!user.getTpin().equalsIgnoreCase(checkTpin)) {
				response.setStatus(false);
				response.setMessage("Invalid TPIN");
				response.setRespCode("01");
				return response;
			}

			mast.setWalletStatus(req.getWalletStatus());
			mast.setModifyBy(req.getUsername().toUpperCase());
			mast.setModifyDt(new Date());
			repository.save(mast);

			response.setStatus(true);
			response.setRespCode("00");
			response.setMessage("Wallet status updated successfully");
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

	public CommonResponse updateUserStatus(UpdateUserStatusRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			UserManagementMaster mast = repository.findByUserId(req.getUserId());
			if (mast == null) {
				response.setStatus(false);
				response.setMessage("User not found");
				response.setRespCode("01");
				return response;
			}

			if (req.getUserStatus().equalsIgnoreCase("Active") && mast.getStatus().equalsIgnoreCase("Active")) {
				response.setStatus(false);
				response.setMessage("User status is already Active");
				response.setRespCode("01");
				return response;
			}

			if (req.getUserStatus().equalsIgnoreCase("Deactive") && mast.getStatus().equalsIgnoreCase("Deactive")) {
				response.setStatus(false);
				response.setMessage("User status is already Deactive");
				response.setRespCode("01");
				return response;
			}

			String checkTpin = commonUtils.hashSHA256(req.getUsername().toUpperCase(), req.getTpin());
			if (!user.getTpin().equalsIgnoreCase(checkTpin)) {
				response.setStatus(false);
				response.setMessage("Invalid TPIN");
				response.setRespCode("01");
				return response;
			}

			mast.setStatus(req.getUserStatus());
			mast.setStatusModifyRemark(req.getRemark());
			mast.setModifyBy(req.getUsername().toUpperCase());
			mast.setModifyDt(new Date());
			repository.save(mast);

			response.setStatus(true);
			response.setRespCode("00");
			response.setMessage("User status updated successfully");
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

	public CommonKycResponse kycSummary(KycSummaryRequst req) {
		CommonKycResponse response = new CommonKycResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			List<Object[]> kycSummaryList = repository.fetchKycStatusCounts(startDate, endDate);

			List<KycStatusCount> kycCount = kycSummaryList.stream()
					.map(result -> new KycStatusCount((String) result[0], ((Number) result[1]).longValue()))
					.collect(Collectors.toList());

			response.setStatus(true);
			response.setRespCode("00");
			response.setMessage("Record found successfully");
			response.setData(kycCount);
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

	public CommonResponse updateCapBal(@Valid UpdateCapBalRequst req) {
		CommonResponse response = new CommonResponse();
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				response.setStatus(false);
				response.setMessage("Invalid Username");
				response.setRespCode("01");
				return response;
			}

			UserManagementMaster mast = repository.findByUserId(req.getUserId());
			if (mast == null) {
				response.setStatus(false);
				response.setMessage("User not found");
				response.setRespCode("01");
				return response;
			}

			String checkTpin = commonUtils.hashSHA256(req.getUsername().toUpperCase(), req.getTpin());
			if (!user.getTpin().equalsIgnoreCase(checkTpin)) {
				response.setStatus(false);
				response.setMessage("Invalid TPIN");
				response.setRespCode("01");
				return response;
			}

			BigDecimal updatedMainWalletBalance = mast.getMainWalletBalance().add(req.getMainWalletBalance());
			BigDecimal updatedAepsWalletBalance = mast.getAepsWalletBalance().add(req.getAepsWalletBalance());

			mast.setMainWalletBalance(updatedMainWalletBalance);
			mast.setAepsWalletBalance(updatedAepsWalletBalance);
			mast.setModifyBy(req.getUsername().toUpperCase());
			mast.setModifyDt(new Date());

			repository.save(mast);

			response.setStatus(true);
			response.setRespCode("00");
			response.setMessage("Wallet balances updated successfully");
			return response;

		} catch (

		Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

	public CommonKycResponse fullKycSummary() {
		CommonKycResponse response = new CommonKycResponse();
		try {

			List<Object[]> kycSummaryList = repository.fetchFullKycStatusCounts();

			List<KycStatusCount> kycCount = kycSummaryList.stream()
					.map(result -> new KycStatusCount((String) result[0], ((Number) result[1]).longValue()))
					.collect(Collectors.toList());

			long totalCount = kycCount.stream().mapToLong(KycStatusCount::getTotalCount).sum();

			response.setStatus(true);
			response.setRespCode("00");
			response.setMessage("Record found successfully");
			response.setTotalCount(totalCount);
			response.setData(kycCount);
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

	public CommonResponse getProfile(@Valid UserManagementFetchRequest req) {
		CommonResponse response = new CommonResponse();
		try {
			// Fetch profiles based on user type from the ProfilesMaster repository
			List<ProfilesMaster> profiles = profilesMasterRepository.findByUserType(req.getUserType());

			if (profiles.isEmpty()) {
				response.setStatus(false);
				response.setMessage("No profiles found for the given user type");
				response.setRespCode("01");
				return response;
			}

			// Extract profile names
			List<String> profileNames = profiles.stream().map(ProfilesMaster::getProfileName)
					.collect(Collectors.toList());

			response.setStatus(true);
			response.setRespCode("00");
			response.setMessage("Profiles fetched successfully");
			response.setData("profileNames", profileNames);
			return response;

		} catch (Exception e) {
			logger.error("Exception: ", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

}
