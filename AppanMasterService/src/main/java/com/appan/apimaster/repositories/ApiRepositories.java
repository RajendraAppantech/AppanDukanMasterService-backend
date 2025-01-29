package com.appan.apimaster.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appan.entity.AndroidSettingMaster;
import com.appan.entity.EmailApiMaster;
import com.appan.entity.KycApiMaster;
import com.appan.entity.SmsApiMaster;
import com.appan.entity.TransactionApiMaster;
import com.appan.entity.WhatsappApiMaster;

@Repository
public class ApiRepositories {

	public interface TransactionApiMasterRepository extends JpaRepository<TransactionApiMaster, Long> {

		TransactionApiMaster findByApiNameAndCode(String apiName, String code);

		Page<TransactionApiMaster> findAll(Specification<TransactionApiMaster> specification, Pageable paging);

		List<TransactionApiMaster> findByAuthStatus(String string);

		TransactionApiMaster findByApiNameAndCodeAndType(String apiName, String code, String type);

		List<TransactionApiMaster> findByStatus(String string);

	}

	public interface SmsApiMasterRepository extends JpaRepository<SmsApiMaster, Long> {

		SmsApiMaster findByTenantNameAndApiUrlIgnoreCase(String trim, String trim2);

		Page<SmsApiMaster> findAll(Specification<SmsApiMaster> specification, Pageable paging);

		SmsApiMaster findByTenantNameAndApiUrl(String tenantName, String apiUrl);

		List<SmsApiMaster> findByStatus(String string);

	}

	public interface KycApiMasterRepository extends JpaRepository<KycApiMaster, Long> {

		KycApiMaster findByApiNameAndCodeIgnoreCase(String trim, String trim2);

		Page<KycApiMaster> findAll(Specification<KycApiMaster> specification, Pageable paging);

		List<KycApiMaster> findByAuthStatus(String string);

		KycApiMaster findByApiNameAndCode(String apiName, String code);

		List<KycApiMaster> findByStatus(String string);

	}

	public interface EmailApiMasterRepository extends JpaRepository<EmailApiMaster, Long> {

		EmailApiMaster findByHostNameAndEmailAddressIgnoreCase(String trim, String trim2);

		Page<EmailApiMaster> findAll(Specification<EmailApiMaster> specification, Pageable paging);

		EmailApiMaster findByEmailAddress(String emailAddress);


	}

	public interface WhatsappApiMasterRepository extends JpaRepository<WhatsappApiMaster, Long> {

		WhatsappApiMaster findByTenantNameAndApiUrl(String tenantName, String apiUrl);

		Page<WhatsappApiMaster> findAll(Specification<WhatsappApiMaster> specification, Pageable paging);

		List<WhatsappApiMaster> findByAuthStatus(String string);

		List<WhatsappApiMaster> findByStatus(String string);

	}

	public interface AndroidSettingMasterRepository extends JpaRepository<AndroidSettingMaster, Long> {

		AndroidSettingMaster findByTenantNameAndSenderId(String tenantName, String senderId);

		Page<AndroidSettingMaster> findAll(Specification<AndroidSettingMaster> specification, Pageable paging);

		List<AndroidSettingMaster> findByAuthStatus(String string);

		AndroidSettingMaster findByTenantNameAndSenderIdAndServerKey(String tenantName, String senderId,
				String serverKey);

		List<AndroidSettingMaster> findByStatus(String string);

	}
}
