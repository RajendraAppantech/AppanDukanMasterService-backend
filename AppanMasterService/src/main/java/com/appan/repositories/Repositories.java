package com.appan.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.appan.bankmaster.entity.CompanyBankAccount;
import com.appan.bankmaster.entity.MasterBankData;
import com.appan.bankmaster.entity.MasterIfsc;
import com.appan.bankmaster.entity.UserBank;
import com.appan.entity.AlertTypeMaster;
import com.appan.entity.BlockPoMaster;
import com.appan.entity.BusinessCategory;
import com.appan.entity.BusinessEntityType;
import com.appan.entity.BusinessSubCategory;
import com.appan.entity.CityMaster;
import com.appan.entity.CountryMaster;
import com.appan.entity.DesignReceiptsMaster;
import com.appan.entity.DistrictMaster;
import com.appan.entity.EmailTemplateMaster;
import com.appan.entity.PasswordHistory;
import com.appan.entity.PaymentModeMaster;
import com.appan.entity.PincodeMaster;
import com.appan.entity.ProfileOperationMaster;
import com.appan.entity.ProfilePriorityMaster;
import com.appan.entity.ProfilesMaster;
import com.appan.entity.RegionMaster;
import com.appan.entity.RegistrationFields;
import com.appan.entity.RegistrationSteps;
import com.appan.entity.RetailerUserMaster;
import com.appan.entity.SendSmsEmailMaster;
import com.appan.entity.ServiceConfigCategoryMaster;
import com.appan.entity.ServiceConfigOperatorGroupingMaster;
import com.appan.entity.ServiceConfigOperatorMaster;
import com.appan.entity.ServiceConfigOperatorParameterMaster;
import com.appan.entity.ServiceConfigOperatorUpdateMaster;
import com.appan.entity.ServiceConfigServiceMaster;
import com.appan.entity.SmsMaster;
import com.appan.entity.SmsTemplateMaster;
import com.appan.entity.SourceOfFund;
import com.appan.entity.StateMaster;
import com.appan.entity.SubPaymentMode;
import com.appan.entity.TaxMaster;
import com.appan.entity.TenantManagementMaster;
import com.appan.entity.TicketCategory;
import com.appan.entity.TicketChildCategory;
import com.appan.entity.TicketKnowledge;
import com.appan.entity.TicketManagementMaster;
import com.appan.entity.TicketSubCategory;
import com.appan.entity.UserMaster;
import com.appan.entity.UserMenu;
import com.appan.entity.UserTypeMaster;
import com.appan.entity.UserTypeOperation;
import com.appan.entity.UsernameFormat;
import com.appan.entity.WalletMaster;

import jakarta.transaction.Transactional;

@Repository
public class Repositories {

	public interface RegionMasteRepository extends JpaRepository<RegionMaster, Long> {

		@Query("SELECT r FROM RegionMaster r WHERE UPPER(r.regionName) = UPPER(:regionName) AND UPPER(r.regionType) = UPPER(:regionType)")
		RegionMaster findByRegionNameAndRegionType(@Param("regionName") String regionName,
				@Param("regionType") String regionType);

		Page<RegionMaster> findAll(Specification<RegionMaster> specification, Pageable paging);

		List<RegionMaster> findByAuthStatus(String string);

		RegionMaster findByRegionId(long regionId);

		RegionMaster findByRegionNameIgnoreCase(String regionName);

		boolean existsByRegionNameIgnoreCase(String regionName);

		List<RegionMaster> findByStatus(String string);

	}

	public interface CountryMasteRepository extends JpaRepository<CountryMaster, Long> {

		@Query("SELECT c FROM CountryMaster c WHERE LOWER(c.countryName) = LOWER(:countryName)")
		CountryMaster findByCountryName(@Param("countryName") String countryName);

		Page<CountryMaster> findAll(Specification<CountryMaster> specification, Pageable paging);

		List<CountryMaster> findByAuthStatus(String string);

		CountryMaster findTopByOrderByIdDesc();

		List<CountryMaster> findByStatus(String string);

	}

	public interface StateMasteRepository extends JpaRepository<StateMaster, Long> {

		StateMaster findByStateName(String trim);

		Page<StateMaster> findAll(Specification<StateMaster> specification, Pageable paging);

		List<StateMaster> findByAuthStatus(String string);

		List<StateMaster> findByStatus(String string);

	}

	public interface DistrictMasteRepository extends JpaRepository<DistrictMaster, Long> {

		Page<DistrictMaster> findAll(Specification<DistrictMaster> specification, Pageable paging);

		List<DistrictMaster> findByAuthStatus(String string);

		DistrictMaster findByDistrictId(Long districtId);

		DistrictMaster findByDistrictNameAndStateNameIgnoreCase(String districtName, String stateName);

		DistrictMaster findByDistrictNameAndStateName(String trim, String trim2);

		List<DistrictMaster> findByStatus(String string);

	}

	public interface CityMasteRepository extends JpaRepository<CityMaster, Long> {

		@Query("SELECT c FROM CityMaster c WHERE LOWER(c.cityName) = LOWER(:cityName) "
				+ "AND LOWER(c.stateName) = LOWER(:stateName) " + "AND LOWER(c.districtName) = LOWER(:districtName)")
		CityMaster findByCityNameAndStateNameAndDistrictNameIgnoreCase(String cityName, String stateName,
				String districtName);

		Page<CityMaster> findAll(Specification<CityMaster> specification, Pageable paging);

		List<CityMaster> findByAuthStatus(String string);

		@Query("SELECT c FROM CityMaster c WHERE LOWER(c.cityName) = LOWER(:cityName) "
				+ "AND LOWER(c.districtName) = LOWER(:districtName) " + "AND LOWER(c.stateName) = LOWER(:stateName)")
		CityMaster findByCityNameAndDistrictNameAndStateName(@Param("cityName") String cityName,
				@Param("districtName") String districtName, @Param("stateName") String stateName);

		List<CityMaster> findByStatus(String string);

		CityMaster findByCityName(String cityName);

	}

	public interface BlockPoMasteRepository extends JpaRepository<BlockPoMaster, Long> {

		@Query("SELECT b FROM BlockPoMaster b WHERE UPPER(b.blockPoName) = UPPER(:blockPoName)")
		BlockPoMaster findByBlockPoName(@Param("blockPoName") String blockPoName);

		Page<BlockPoMaster> findAll(Specification<BlockPoMaster> specification, Pageable paging);

		List<BlockPoMaster> findByAuthStatus(String string);

		@Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END "
				+ "FROM BlockPoMaster b WHERE LOWER(b.blockPoName) = LOWER(:blockPoName) "
				+ "AND LOWER(b.cityName) = LOWER(:cityName)")
		boolean existsByBlockPoNameAndCityNameIgnoreCase(String blockPoName, String cityName);

		List<BlockPoMaster> findByStatus(String string);

	}

	public interface PincodeMasteRepository extends JpaRepository<PincodeMaster, Long> {

		PincodeMaster findByPincode(String pincode);

		Page<PincodeMaster> findAll(Specification<PincodeMaster> specification, Pageable paging);

		List<PincodeMaster> findByAuthStatus(String string);

		boolean existsByPincodeIgnoreCase(String pincode);

		boolean existsByBlockPoNameIgnoreCase(String blockPoName);

		boolean existsByPincodeIgnoreCaseAndBlockPoNameIgnoreCaseAndStatusIgnoreCaseAndPincodeIdNot(String pincode,
				String blockPoName, String status, Long pincodeId);

		List<PincodeMaster> findByStatus(String string);

	}

	public interface BusinessEntityTypeRepository extends JpaRepository<BusinessEntityType, Long> {

		Page<BusinessEntityType> findAll(Specification<BusinessEntityType> specification, Pageable paging);

		@Query("SELECT b FROM BusinessEntityType b WHERE UPPER(b.entityTypeName) = UPPER(:entityTypeName) OR UPPER(b.code) = UPPER(:code)")
		BusinessEntityType findByEntityTypeNameOrCode(@Param("entityTypeName") String entityTypeName,
				@Param("code") String code);

		List<BusinessEntityType> findByAuthStatus(String string);

		boolean existsByCodeIgnoreCase(String normalizedEntityTypeCode);

		List<BusinessEntityType> findByStatus(String string);
	}

	public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, Long> {

		boolean existsByCategoryCodeIgnoreCase(String categoryCode);

		Page<BusinessCategory> findAll(Specification<BusinessCategory> specification, Pageable paging);

		@Query("SELECT b FROM BusinessCategory b WHERE LOWER(b.categoryName) = LOWER(:categoryName) AND LOWER(b.categoryCode) = LOWER(:categoryCode)")
		BusinessCategory findByCategoryNameAndCategoryCodeIgnoreCase(@Param("categoryName") String categoryName,
				@Param("categoryCode") String categoryCode);

		List<BusinessCategory> findByAuthStatus(String string);

		BusinessCategory findTopByOrderByIdDesc();

		List<BusinessCategory> findByStatus(String string);

	}

	public interface BusinessSubCategoryRepository extends JpaRepository<BusinessSubCategory, Long> {

		boolean existsByCategoryNameIgnoreCaseAndSubCategoryNameIgnoreCase(String categoryName, String subCategoryName);

		Page<BusinessSubCategory> findAll(Specification<BusinessSubCategory> specification, Pageable paging);

		List<BusinessSubCategory> findByAuthStatus(String string);

		BusinessSubCategory findBySubCategoryNameOrCategoryCodeIgnoreCase(String subCategoryName, String categoryCode);

		BusinessSubCategory findTopByOrderByIdDesc();

		List<BusinessSubCategory> findByStatus(String string);

	}

	public interface CompanyBankAccountRepository extends JpaRepository<CompanyBankAccount, Long> {

		boolean existsByAccNumberAndBankName(String accNumber, String bankName);

		Page<CompanyBankAccount> findAll(Specification<CompanyBankAccount> specification, Pageable paging);

		List<CompanyBankAccount> findByAuthStatus(String authStatus);

		CompanyBankAccount findTopByOrderByIdDesc();

	}

	public interface MasterIfscRepository extends JpaRepository<MasterIfsc, Long> {

		MasterIfsc findByIfscCode(String trim);

		Page<MasterIfsc> findAll(Specification<MasterIfsc> specification, Pageable paging);

		List<MasterIfsc> findByAuthStatus(String string);

		boolean existsByIfscCodeIgnoreCase(String ifscCode);

		MasterIfsc findTopByOrderByIdDesc();
	}

	public interface MasterBankDataRepository extends JpaRepository<MasterBankData, Long> {

		boolean existsByBankNameAndIfscCodeAndBranch(String bankName, String ifscCode, String branch);

		Page<MasterBankData> findAll(Specification<MasterBankData> specification, Pageable paging);

		MasterBankData findByBankNameAndIfscCode(String bankName, String ifscCode);

		List<MasterBankData> findByAuthStatus(String string);

	}

	public interface UserBankRepository extends JpaRepository<UserBank, Long> {

		Optional<UserBank> findByAccNumberAndIfscCode(String accNumber, String ifscCode);

		UserBank findByAccNumberAndBankCode(String accNumber, String bankCode);

		Page<UserBank> findAll(Specification<UserBank> specification, Pageable paging);

		List<UserBank> findByAuthStatus(String string);

		UserBank findTopByOrderByIdDesc();
	}

	public interface TaxMasterRepository extends JpaRepository<TaxMaster, Long> {

		Page<TaxMaster> findAll(Specification<TaxMaster> specification, Pageable paging);

		List<TaxMaster> findByAuthStatus(String string);

		TaxMaster findByTaxTypeName(String taxTypeName);

	}

	public interface UserMasterRepository extends JpaRepository<UserMaster, String> {

		UserMaster findByUserId(String userId);

		UserMaster findByMobileNo(String mobileNo);

		Page<UserMaster> findAll(Specification<UserMaster> specification, Pageable paging);

		@Query("SELECT u.userId FROM UserMaster u WHERE u.userRole = :userRole AND u.userProfile = :userProfile AND u.authStatus = '1'")
		List<String> findUserIdsByRoleAndProfile(@Param("userRole") String userRole,
				@Param("userProfile") String userProfile);

		@Query("SELECT u.userMenu FROM UserMaster u WHERE u.userId = :userId AND u.userRole = :userRole AND u.userProfile = :userProfile")
		String findUserMenuByUserIdRoleAndProfile(@Param("userId") String userId, @Param("userRole") String userRole,
				@Param("userProfile") String userProfile);

		@Modifying
		@Transactional
		@Query("UPDATE UserMaster u SET u.userMenu = :userMenu WHERE u.userId = :userId AND u.userRole = :userRole AND u.userProfile = :userProfile")
		int updateUserMenuByUserIdRoleAndProfile(@Param("userId") String userId, @Param("userRole") String userRole,
				@Param("userProfile") String userProfile, @Param("userMenu") String userMenu);

		@Query("SELECT u.userId FROM UserMaster u")
		List<String> findAllUsernames();

		@Query("SELECT COUNT(u.sessionId) FROM UserMaster u WHERE u.sessionId = :sessionId")
		Long countSessionBySessionId(@Param("sessionId") UUID sessionId);

	}

	public interface RetailerUserMasterRepository extends JpaRepository<RetailerUserMaster, String> {
	}

	public interface UserMenuRepository extends JpaRepository<UserMenu, Long> {
		UserMenu findByUserRoleAndUserProfile(String userRole, String userProfile);

		boolean existsByUserProfileAndUserRole(String userProfile, String userRole);

		Page<UserMenu> findAll(Specification<UserMenu> specification, Pageable paging);

		UserMenu findByUserProfileAndUserRole(String upperCase, String upperCase2);

		UserMenu findByUserProfileAndUserRoleAndMenuIdNot(String upperCase, String upperCase2, Long menuId);

		List<UserMenu> findByUserProfileAndAuthStatus(String userProfile, String authStatus);

		@Query("SELECT u.menu FROM UserMenu u WHERE u.userRole = :userRole AND u.userProfile = :userProfile")
		List<String> findMenuByRoleAndProfile(@Param("userRole") String userRole,
				@Param("userProfile") String userProfile);

		@Modifying
		@Transactional
		@Query("UPDATE UserMenu m SET m.menu = :userMenu WHERE m.userRole = :userRole AND m.userProfile = :userProfile")
		int updateMenuByRoleAndProfile(@Param("userRole") String role, @Param("userProfile") String profile,
				@Param("userMenu") String userMenu);

		@Query("SELECT um.roleName FROM UserMenu um WHERE um.userRole = :userRole AND um.userProfile = :userProfile")
		String findRoleNameByUserRoleAndUserProfile(@Param("userRole") String userRole,
				@Param("userProfile") String userProfile);

		List<UserMenu> findByAuthStatus(String string);

		@Query("SELECT u.userProfile FROM UserMenu u")
		List<String> findAllUserProfiles();

		UserMenu findByRoleName(String userMenuType);

	}

	public interface SmsMasterRepository extends JpaRepository<SmsMaster, Long> {

		@Query("SELECT COUNT(CASE WHEN s.status = 'C' THEN 1 END), " + "COUNT(CASE WHEN s.status = 'F' THEN 1 END), "
				+ "COUNT(CASE WHEN s.status IN ('T', 'P') THEN 1 END), "
				+ "(COUNT(CASE WHEN s.status = 'C' THEN 1 END) + " + "COUNT(CASE WHEN s.status = 'F' THEN 1 END) + "
				+ "COUNT(CASE WHEN s.status IN ('T', 'P') THEN 1 END)) "
				+ "FROM sms_master s WHERE s.otpDate BETWEEN :startDate AND :endDate")
		List<Object[]> getSmsOverview(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

		@Query("SELECT COUNT(CASE WHEN s.status = 'C' THEN 1 END), " + "COUNT(CASE WHEN s.status = 'F' THEN 1 END), "
				+ "COUNT(CASE WHEN s.status IN ('T', 'P') THEN 1 END), "
				+ "(COUNT(CASE WHEN s.status = 'C' THEN 1 END) + " + "COUNT(CASE WHEN s.status = 'F' THEN 1 END) + "
				+ "COUNT(CASE WHEN s.status IN ('T', 'P') THEN 1 END)) "
				+ "FROM sms_master s WHERE s.otpDate BETWEEN :startDate AND :endDate " + "AND s.smsKey = :authKey")
		List<Object[]> getSmsOverviewCustomer(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
				@Param("authKey") String authKey);

		@Query("SELECT COUNT(CASE WHEN s.status = 'C' THEN 1 END), " + "COUNT(CASE WHEN s.status = 'F' THEN 1 END), "
				+ "COUNT(CASE WHEN s.status IN ('T', 'P') THEN 1 END), "
				+ "(COUNT(CASE WHEN s.status = 'C' THEN 1 END) + " + "COUNT(CASE WHEN s.status = 'F' THEN 1 END) + "
				+ "COUNT(CASE WHEN s.status IN ('T', 'P') THEN 1 END)) "
				+ "FROM sms_master s WHERE s.otpDate BETWEEN :startDate AND :endDate " + "AND s.username = :vendorCode")
		List<Object[]> getSmsOverviewVendor(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
				@Param("vendorCode") String vendorCode);

		@Query(value = "SELECT TO_CHAR(DATE_TRUNC('month', (make_date(:year, 1, 1) + (interval '1 month' * s.number))), 'Month') AS monthName, "
				+ "COALESCE(SUM(CASE WHEN sms.status = 'C' THEN 1 END), 0) AS completed, "
				+ "COALESCE(SUM(CASE WHEN sms.status = 'F' THEN 1 END), 0) AS rejected, "
				+ "COALESCE(SUM(CASE WHEN sms.status IN ('P', 'T') THEN 1 END), 0) AS pending, "
				+ "COALESCE(SUM(CASE WHEN sms.status IN ('C', 'F', 'P', 'T') THEN 1 END), 0) AS totalSms "
				+ "FROM generate_series(0, 11) AS s(number) "
				+ "LEFT JOIN sms_master AS sms ON EXTRACT(MONTH FROM sms.otp_date) = s.number + 1 AND EXTRACT(YEAR FROM sms.otp_date) = :year "
				+ "GROUP BY s.number " + "ORDER BY s.number", nativeQuery = true)
		List<Object[]> findMonthlySmsSummaryByYear(@Param("year") int year);

		@Query(value = "SELECT TO_CHAR(DATE_TRUNC('month', (make_date(:year, 1, 1) + (interval '1 month' * s.number))), 'Month') AS monthName, "
				+ "COALESCE(SUM(CASE WHEN sms.status = 'C' THEN 1 END), 0) AS completed, "
				+ "COALESCE(SUM(CASE WHEN sms.status = 'F' THEN 1 END), 0) AS rejected, "
				+ "COALESCE(SUM(CASE WHEN sms.status IN ('P', 'T') THEN 1 END), 0) AS pending, "
				+ "COALESCE(SUM(CASE WHEN sms.status IN ('C', 'F', 'P', 'T') THEN 1 END), 0) AS totalSms "
				+ "FROM generate_series(0, 11) AS s(number) "
				+ "LEFT JOIN sms_master AS sms ON EXTRACT(MONTH FROM sms.otp_date) = s.number + 1 "
				+ "AND EXTRACT(YEAR FROM sms.otp_date) = :year " + "AND sms.username = :username "
				+ "GROUP BY s.number " + "ORDER BY s.number", nativeQuery = true)
		List<Object[]> findMonthlySmsSummaryByYearByUserwise(@Param("year") int year,
				@Param("username") String username);

		Page<SmsMaster> findAll(Specification<SmsMaster> specification, Pageable paging);

		@Query("SELECT " + "COUNT(CASE WHEN s.status = 'C' THEN 1 END) AS smsCompleted, "
				+ "COUNT(CASE WHEN s.status IN ('P', 'S') THEN 1 END) AS smsPending, "
				+ "COUNT(CASE WHEN s.status = 'F' THEN 1 END) AS smsRejected " + "FROM sms_master s "
				+ "WHERE s.otpDate BETWEEN :startDate AND :endDate")
		List<Object[]> getProgressCircleData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

		@Query("SELECT COUNT(CASE WHEN s.status = 'C' THEN 1 END) AS smsCompleted, "
				+ "COUNT(CASE WHEN s.status IN ('P', 'S') THEN 1 END) AS smsPending, "
				+ "COUNT(CASE WHEN s.status = 'F' THEN 1 END) AS smsRejected " + "FROM sms_master s "
				+ "WHERE s.otpDate BETWEEN :startDate AND :endDate AND s.smsKey = :authKey")
		List<Object[]> getProgressCircleDataCus(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
				@Param("authKey") String authKey);

		@Query("SELECT COUNT(CASE WHEN s.status = 'C' THEN 1 END) AS smsCompleted, "
				+ "COUNT(CASE WHEN s.status IN ('P', 'S') THEN 1 END) AS smsPending, "
				+ "COUNT(CASE WHEN s.status = 'F' THEN 1 END) AS smsRejected " + "FROM sms_master s "
				+ "WHERE s.otpDate BETWEEN :startDate AND :endDate "
				+ "AND (:vendorCode IS NULL OR s.username = :vendorCode)")
		List<Object[]> getProgressCircleDataVen(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
				@Param("vendorCode") Optional<String> vendorCode);

	}

	public interface WalletMasterRepository extends JpaRepository<WalletMaster, Long> {

		@Query("SELECT w FROM WalletMaster w WHERE LOWER(w.walletName) = LOWER(:walletName) AND LOWER(w.code) = LOWER(:code)")
		WalletMaster findByWalletNameAndCodeIgnoreCase(@Param("walletName") String walletName,
				@Param("code") String code);

		Page<WalletMaster> findAll(Specification<WalletMaster> specification, Pageable paging);

		List<WalletMaster> findByAuthStatus(String string);

		@Query("SELECT w FROM WalletMaster w WHERE w.walletName = :walletName OR w.code = :code")
		WalletMaster findByWalletNameOrCode(@Param("walletName") String walletName, @Param("code") String code);

		List<WalletMaster> findByStatus(String string);

	}

	public interface SourceOfFundRepository extends JpaRepository<SourceOfFund, Long> {

		SourceOfFund findBySourceNameAndCodeIgnoreCase(String sourceName, String code);

		List<SourceOfFund> findByAuthStatus(String string);

		Page<SourceOfFund> findAll(Specification<SourceOfFund> specification, Pageable paging);

		SourceOfFund findBySourceNameAndCategoryName(String sourceName, String categoryName);

		List<SourceOfFund> findByStatus(String string);

	}

	public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {

		Page<TicketCategory> findAll(Specification<TicketCategory> specification, Pageable paging);

		List<TicketCategory> findByAuthStatus(String string);

		TicketCategory findByTicketCategoryId(Long ticketCategoryId);

		TicketCategory findByCategoryName(String categoryName);

		@Query("SELECT MAX(tc.ticketCategoryId) FROM TicketCategory tc")
		Optional<Long> findMaxTicketCategoryId();

		List<TicketCategory> findByStatus(String string);

	}

	public interface TicketSubCategoryRepository extends JpaRepository<TicketSubCategory, Long> {

		Page<TicketSubCategory> findAll(Specification<TicketSubCategory> specification, Pageable paging);

		List<TicketSubCategory> findByAuthStatus(String string);

		TicketSubCategory findByTicketSubCategoryId(Long ticketSubCategoryId);

		TicketSubCategory findBySubCategoryName(String subCategoryName);

		@Query("SELECT COALESCE(MAX(t.ticketSubCategoryId), 0) FROM TicketSubCategory t")
		Optional<Long> findMaxTicketSubCategoryId();

		List<TicketSubCategory> findByStatus(String string);

	}

	public interface TicketChildCategoryRepository extends JpaRepository<TicketChildCategory, Long> {

		Page<TicketChildCategory> findAll(Specification<TicketChildCategory> specification, Pageable paging);

		List<TicketChildCategory> findByAuthStatus(String string);

		TicketChildCategory findByTicketChildCategoryId(Long ticketChildCategoryId);

		TicketChildCategory findByChildCategoryName(String childCategoryName);

		@Query("SELECT MAX(t.ticketChildCategoryId) FROM TicketChildCategory t")
		Optional<Long> findMaxTicketChildCategoryId();

	}

	public interface TicketKnowledgeRepository extends JpaRepository<TicketKnowledge, Long> {

		Page<TicketKnowledge> findAll(Specification<TicketKnowledge> specification, Pageable paging);

		List<TicketKnowledge> findByAuthStatus(String string);

		TicketKnowledge findByTicketKnowledgeId(Long ticketKnowledgeId);

		TicketKnowledge findByKnowledgeCategoryName(String knowledgeCategoryName);

		@Query("SELECT MAX(t.ticketKnowledgeId) FROM TicketKnowledge t")
		Optional<Long> findMaxTicketKnowledgeId();

		List<TicketKnowledge> findByStatus(String string);

	}

	public interface ProfilesMasterRepository extends JpaRepository<ProfilesMaster, Long> {

		ProfilesMaster findByProfileNameAndCodeIgnoreCase(String trim, String trim2);

		Page<ProfilesMaster> findAll(Specification<ProfilesMaster> specification, Pageable paging);

		List<ProfilesMaster> findByAuthStatus(String string);

		boolean existsByProfileNameIgnoreCaseAndCodeIgnoreCaseAndIdNot(String profileName, String code, Long id);

		List<ProfilesMaster> findByStatus(String string);

		List<ProfilesMaster> findByProfileName(String profile);

		List<ProfilesMaster> findByUserType(String userType);

	}

	public interface ProfileOperationMasterRepository extends JpaRepository<ProfileOperationMaster, Long> {

		Page<ProfileOperationMaster> findAll(Specification<ProfileOperationMaster> specification, Pageable paging);

		List<ProfileOperationMaster> findByAuthStatus(String string);

		boolean existsByProfileNameIgnoreCaseAndIdNot(String profileName, Long id);

		ProfileOperationMaster findByOperationNameAndProfileName(String trim, String trim2);

		List<ProfileOperationMaster> findByStatus(String string);

	}

	public interface ProfilePriorityMasterRepository extends JpaRepository<ProfilePriorityMaster, Long> {

		ProfilePriorityMaster findByUserTypeAndUserName(String trim, String trim2);

		Page<ProfilePriorityMaster> findAll(Specification<ProfilePriorityMaster> specification, Pageable paging);

		List<ProfilePriorityMaster> findByStatus(String string);

	}

	public interface PaymentModeMasterRepository extends JpaRepository<PaymentModeMaster, Long> {

		PaymentModeMaster findByPaymentMode(String paymentMode);

		Page<PaymentModeMaster> findAll(Specification<PaymentModeMaster> specification, Pageable paging);

		boolean existsByPaymentModeIgnoreCaseAndIdNot(String paymentMode, Long id);

		PaymentModeMaster findTopByOrderByIdDesc();

		List<PaymentModeMaster> findByStatus(String string);
	}

	public interface SubPaymentModeRepository extends JpaRepository<SubPaymentMode, Long> {

		SubPaymentMode findByPaymentModeAndSubPaymentMode(String paymentMode, String subPaymentMode);

		Page<SubPaymentMode> findAll(Specification<SubPaymentMode> specification, Pageable paging);

		boolean existsBySubPaymentModeIgnoreCaseAndPaymentModeIgnoreCaseAndIdNot(String subPaymentMode,
				String paymentMode, Long id);

		SubPaymentMode findTopByOrderByIdDesc();

		List<SubPaymentMode> findByStatus(String string);
	}

	public interface StepsMasterRepository extends JpaRepository<RegistrationSteps, Long> {

		// @Query("SELECT w FROM RegistrationSteps w WHERE
		// LOWER(w.registration_steps_name) = LOWER(:registration_steps_name) AND
		// LOWER(w.registration_steps_label) = LOWER(:registration_steps_label)")
		RegistrationSteps findByStepsNameAndLabelIgnoreCase(String stepsName, String label);

		Page<RegistrationSteps> findAll(Specification<RegistrationSteps> specification, Pageable paging);

		List<RegistrationSteps> findByAuthStatus(String string);

		// @Query("SELECT w FROM stepsName w WHERE w.registration_steps_name =
		// :stepsName OR w.registration_steps_label = :label")
		RegistrationSteps findByStepsNameAndLabel(String stepsName, String label);

	}

	public interface FieldsMasterRepository extends JpaRepository<RegistrationFields, Long> {

		// @Query("SELECT w FROM RegistrationSteps w WHERE
		// LOWER(w.registration_steps_name) = LOWER(:registration_steps_name) AND
		// LOWER(w.registration_steps_label) = LOWER(:registration_steps_label)")
		RegistrationFields findByFieldsNameAndFieldsTypeIgnoreCase(String fieldsName, String fieldsType);

		Page<RegistrationFields> findAll(Specification<RegistrationFields> specification, Pageable paging);

		// @Query("SELECT w FROM stepsName w WHERE w.registration_steps_name =
		// :stepsName OR w.registration_steps_label = :label")
		RegistrationFields findByfieldsNameAndFieldsType(String fieldsName, String fieldsType);

		List<RegistrationFields> findByStatus(String string);

	}

	public interface ServiceConfigCategoryRepository extends JpaRepository<ServiceConfigCategoryMaster, Long> {

		ServiceConfigCategoryMaster findByCategoryNameAndCategoryCodeIgnoreCase(String categoryName,
				String categoryCode);

		Page<ServiceConfigCategoryMaster> findAll(Specification<ServiceConfigCategoryMaster> specification,
				Pageable paging);

		List<ServiceConfigCategoryMaster> findByAuthStatus(String string);

		ServiceConfigCategoryMaster findByCategoryNameAndCategoryCode(String categoryName, String categoryCode);

		@Query("SELECT MAX(t.id) FROM ServiceConfigCategoryMaster t")
		Optional<Long> findMaxId();

		List<ServiceConfigCategoryMaster> findByStatus(String string);

	}

	public interface ServiceConfigServiceRepository extends JpaRepository<ServiceConfigServiceMaster, Long> {

		ServiceConfigServiceMaster findByServiceNameAndServiceCodeIgnoreCase(String ServiceName, String ServiceCode);

		Page<ServiceConfigServiceMaster> findAll(Specification<ServiceConfigServiceMaster> specification,
				Pageable paging);

		List<ServiceConfigServiceMaster> findByAuthStatus(String string);

		ServiceConfigServiceMaster findByServiceNameAndServiceCode(String ServiceName, String ServiceCode);

		@Query("SELECT MAX(tc.id) FROM ServiceConfigServiceMaster tc")
		Optional<Long> findMaxId();

		List<ServiceConfigServiceMaster> findByStatus(String string);

	}

	public interface ServiceConfigOperatorRepository extends JpaRepository<ServiceConfigOperatorMaster, Long> {

		ServiceConfigOperatorMaster findByServiceNameAndOperatorNameIgnoreCase(String ServiceName, String OperatorName);

		Page<ServiceConfigOperatorMaster> findAll(Specification<ServiceConfigOperatorMaster> specification,
				Pageable paging);

		List<ServiceConfigOperatorMaster> findByAuthStatus(String string);

		ServiceConfigOperatorMaster findByServiceNameAndOperatorName(String ServiceName, String OperatorName);

		@Query("SELECT MAX(tc.id) FROM ServiceConfigOperatorMaster tc")
		Optional<Long> findMaxId();

		List<ServiceConfigOperatorMaster> findByStatus(String string);

	}

	public interface ServiceConfigOperatorUpdateRepository
			extends JpaRepository<ServiceConfigOperatorUpdateMaster, Long> {

		ServiceConfigOperatorUpdateMaster findByApiOperatorCode1AndOperatorNameIgnoreCase(String apiOperatorCode1,
				String OperatorName);

		Page<ServiceConfigOperatorUpdateMaster> findAll(Specification<ServiceConfigOperatorUpdateMaster> specification,
				Pageable paging);

		List<ServiceConfigOperatorUpdateMaster> findByAuthStatus(String string);

		ServiceConfigOperatorUpdateMaster findByApiOperatorCode1AndOperatorName(String apiOperatorCode1,
				String OperatorName);

	}

	public interface ServiceConfigOperatorParameterRepository
			extends JpaRepository<ServiceConfigOperatorParameterMaster, Long> {

		ServiceConfigOperatorParameterMaster findByParameterNameAndOperatorNameIgnoreCase(String ParameterName,
				String OperatorName);

		Page<ServiceConfigOperatorParameterMaster> findAll(
				Specification<ServiceConfigOperatorParameterMaster> specification, Pageable paging);

		List<ServiceConfigOperatorParameterMaster> findByAuthStatus(String string);

		ServiceConfigOperatorParameterMaster findByParameterNameAndOperatorName(String ParameterName,
				String OperatorName);

	}

	public interface ServiceConfigOperatorGroupingRepository
			extends JpaRepository<ServiceConfigOperatorGroupingMaster, Long> {

		ServiceConfigOperatorGroupingMaster findByGroupNameAndOperatorNameAndParameterNameIgnoreCase(String groupName,
				String OperatorName, String ParameterName);

		Page<ServiceConfigOperatorGroupingMaster> findAll(
				Specification<ServiceConfigOperatorGroupingMaster> specification, Pageable paging);

		List<ServiceConfigOperatorGroupingMaster> findByAuthStatus(String string);

		ServiceConfigOperatorGroupingMaster findByGroupNameAndOperatorNameAndParameterName(String groupName,
				String OperatorName, String ParameterName);

		List<ServiceConfigOperatorGroupingMaster> findByStatus(String string);

	}

	public interface TicketManagementMasterRepository extends JpaRepository<TicketManagementMaster, Long> {

		Page<TicketManagementMaster> findAll(Specification<TicketManagementMaster> specification, Pageable paging);

		TicketManagementMaster findBySubject(String subject);

	}

	public interface PasswordExpRepository extends JpaRepository<PasswordHistory, String> {

		@Query(value = "SELECT * FROM passwd_history WHERE user_id = :userId ORDER BY change_time DESC LIMIT 3", nativeQuery = true)
		List<PasswordHistory> fetchPassword(@Param("userId") String userId);

		@Query(value = "SELECT * FROM passwd_history WHERE user_id = :userId AND old_passwd = :newpass ORDER BY change_time DESC LIMIT 3", nativeQuery = true)
		public List<PasswordHistory> fetchPasswordAndOldPasswd(@Param(value = "userId") String userId,
				@Param(value = "newpass") String newpass);

		@Query(value = "SELECT * FROM passwd_history WHERE user_id = :userId AND old_passwd = :newpass AND passwd_type = :paswordType ORDER BY change_time DESC LIMIT 3", nativeQuery = true)
		public List<PasswordHistory> fetchTpinAndOldTpin(@Param(value = "userId") String userId,
				@Param(value = "newpass") String newpass, @Param(value = "paswordType") String paswordType);
	}

	public interface AlertTypeMasterRepository extends JpaRepository<AlertTypeMaster, Long> {

		AlertTypeMaster findByAlertNameAndParametersNameIgnoreCase(String alertName, String parametersName);

		Page<AlertTypeMaster> findAll(Specification<AlertTypeMaster> specification, Pageable parametersName);

		List<AlertTypeMaster> findByAuthStatus(String string);

		AlertTypeMaster findByAlertNameAndParametersName(String alertName, String OperatorName);

	}

	public interface SmsTemplateMasterRepository extends JpaRepository<SmsTemplateMaster, Long> {

		SmsTemplateMaster findBySmsTypeAndTemplateIdIgnoreCase(String smsType, String templateId);

		Page<SmsTemplateMaster> findAll(Specification<SmsTemplateMaster> specification, Pageable parametersName);

		List<SmsTemplateMaster> findByAuthStatus(String string);

		SmsTemplateMaster findBySmsTypeAndTemplateId(String smsType, String templateId);

	}

	public interface EmailTemplateMasterRepository extends JpaRepository<EmailTemplateMaster, Long> {

		EmailTemplateMaster findByEmailTypeAndSubjectIgnoreCase(String emailType, String subject);

		Page<EmailTemplateMaster> findAll(Specification<EmailTemplateMaster> specification, Pageable parametersName);

		List<EmailTemplateMaster> findByAuthStatus(String string);

		EmailTemplateMaster findByEmailTypeAndSubject(String emailType, String subject);

	}

	public interface UserTypeOperationRepository extends JpaRepository<UserTypeOperation, Long> {

		UserTypeOperation findByOperationNameAndUserTypeName(String upperCase, String upperCase2);

		List<UserTypeOperation> findByAuthStatus(String string);

		Page<UserTypeOperation> findAll(Specification<UserTypeOperation> specification, Pageable paging);
	}

	public interface UsernameFormatRepository extends JpaRepository<UsernameFormat, Long> {

		UsernameFormat findByPrefix(String prefix);

		Page<UsernameFormat> findAll(Specification<UsernameFormat> specification, Pageable paging);

		List<UsernameFormat> findByAuthStatus(String string);

		UsernameFormat findByUsernameFormatAndUserTypeAndPrefix(String usernameFormat, String userType, String prefix);

		@Query("SELECT u.prefix FROM UsernameFormat u WHERE u.userType = ?1")
		String findPrefixByUserType(String userType);

	}

	public interface SendSmsEmailMasterRepository extends JpaRepository<SendSmsEmailMaster, Long> {

	}

	public interface DesignReceiptsMasterRepository extends JpaRepository<DesignReceiptsMaster, Long> {

		DesignReceiptsMaster findByDesignAndCategory(String design, String category);

		Page<DesignReceiptsMaster> findAll(Specification<DesignReceiptsMaster> specification, Pageable paging);

	}

	public interface TenantManagementMasterRepository extends JpaRepository<TenantManagementMaster, Long> {

		TenantManagementMaster findByTenantNameIgnoreCase(String trim);

		Page<TenantManagementMaster> findAll(Specification<TenantManagementMaster> specification, Pageable paging);

		List<TenantManagementMaster> findByStatus(String string);

	}

	public interface UserTypeMasterRepository extends JpaRepository<UserTypeMaster, Long> {

		@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " + "FROM UserTypeMaster u "
				+ "WHERE LOWER(u.userType) = LOWER(:userType) AND LOWER(u.userCode) = LOWER(:userCode)")
		boolean existsByUserTypeAndUserCodeIgnoreCase(@Param("userType") String userType,
				@Param("userCode") String userCode);

		Page<UserTypeMaster> findAll(Specification<UserTypeMaster> specification, Pageable paging);

		List<UserTypeMaster> findByStatus(String string);

		UserTypeMaster findByUserType(String userType);

	}

}
