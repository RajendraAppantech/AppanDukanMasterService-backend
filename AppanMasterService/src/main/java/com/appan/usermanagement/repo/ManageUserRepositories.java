package com.appan.usermanagement.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.appan.entity.BackOfficeManagement;
import com.appan.entity.UserManagementMaster;

@Repository
public class ManageUserRepositories {

	public interface UserManagementMasterRepository extends JpaRepository<UserManagementMaster, Long> {

		Page<UserManagementMaster> findAll(Specification<UserManagementMaster> specification, Pageable parametersName);

		@Query("SELECT um.userId FROM UserManagementMaster um ORDER BY um.id DESC LIMIT 1")
		String findLastUserId();

		UserManagementMaster findByUserId(String userId);

		List<UserManagementMaster> findByMobileNumber(String mobileNo);

		List<UserManagementMaster> findByEmailId(String emailId);

		@Query(value = "WITH kyc_statuses AS (SELECT 'Approved' AS kyc_status "
				+ " UNION ALL SELECT 'Registered' UNION ALL SELECT 'Accepted' "
				+ " UNION ALL SELECT 'Submitted' UNION ALL SELECT 'Rejected') "
				+ " SELECT ks.kyc_status, COALESCE(COUNT(umm.created_dt), 0) AS total_count " + " FROM kyc_statuses ks "
				+ " LEFT JOIN appan_dukan.user_management_master umm  ON ks.kyc_status = umm.kyc_status "
				+ " AND umm.created_dt BETWEEN :startDate AND :endDate "
				+ " GROUP BY ks.kyc_status ORDER BY ks.kyc_status;", nativeQuery = true)
		List<Object[]> fetchKycStatusCounts(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

		@Query(value = "WITH kyc_statuses AS (SELECT 'Approved' AS kyc_status "
				+ " UNION ALL SELECT 'Registered' UNION ALL SELECT 'Accepted' "
				+ " UNION ALL SELECT 'Submitted' UNION ALL SELECT 'Rejected') "
				+ " SELECT ks.kyc_status, COALESCE(COUNT(umm.created_dt), 0) AS total_count " + " FROM kyc_statuses ks "
				+ " LEFT JOIN appan_dukan.user_management_master umm  ON ks.kyc_status = umm.kyc_status "
				+ " GROUP BY ks.kyc_status ORDER BY ks.kyc_status;", nativeQuery = true)
		List<Object[]> fetchFullKycStatusCounts();

		List<UserManagementMaster> findByUserType(String userType);

		UserManagementMaster findTopByOrderByIdDesc();

		List<UserManagementMaster> findByStatus(String string);

		List<UserManagementMaster> findByUserTypeIn(List<String> filteredUserTypes);

		@Query("SELECT userId FROM UserManagementMaster WHERE userType = :userType ORDER BY id DESC LIMIT 1")
		String findLastUserIdByUserType(@Param("userType") String userType);

		boolean existsByMobileNumber(String mobileNumber);


	}

	public interface BackOfficeManagementRepository extends JpaRepository<BackOfficeManagement, Long> {

		@Query("SELECT b.userId FROM BackOfficeManagement b WHERE b.userType = :userType ORDER BY b.createdDt DESC LIMIT 1")
		String findLastUserIdByUserType(String userType);

		Page<BackOfficeManagement> findAll(Specification<BackOfficeManagement> specification, Pageable parametersName);

		List<BackOfficeManagement> findByStatus(String string);
	}

}
