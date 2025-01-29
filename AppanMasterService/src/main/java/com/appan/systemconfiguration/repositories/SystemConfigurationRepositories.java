package com.appan.systemconfiguration.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appan.entity.SystemConfigChannel;
import com.appan.entity.SystemConfigErrorDefination;
import com.appan.entity.SystemConfigOperation;
import com.appan.entity.SystemConfigOperationType;
import com.appan.entity.SystemConfigTxnType;

@Repository
public class SystemConfigurationRepositories {

	public interface SystemConfigChannelRepository extends JpaRepository<SystemConfigChannel, Long> {

		List<SystemConfigChannel> findByAuthStatus(String string);

		SystemConfigChannel findByChannelName(String channelName);

		Page<SystemConfigChannel> findAll(Specification<SystemConfigChannel> specification, Pageable paging);

		SystemConfigChannel findByChannelNameIgnoreCase(String channelName);

		List<SystemConfigChannel> findByStatus(String string);

	}

	public interface SystemConfigTxnTypeRepository extends JpaRepository<SystemConfigTxnType, Long> {

		List<SystemConfigTxnType> findByAuthStatus(String string);

		Page<SystemConfigTxnType> findAll(Specification<SystemConfigTxnType> specification, Pageable paging);

		SystemConfigTxnType findByCode(String upperCase);

		SystemConfigTxnType findByTxnTypeAndCode(String txnType, String code);

		List<SystemConfigTxnType> findByStatus(String string);

	}

	public interface SystemConfigOperationTypeRepository extends JpaRepository<SystemConfigOperationType, Long> {

		List<SystemConfigOperationType> findByAuthStatus(String string);

		Page<SystemConfigOperationType> findAll(Specification<SystemConfigOperationType> specification,
				Pageable paging);

		SystemConfigOperationType findByOperationTypeIgnoreCase(String operationType);

		SystemConfigOperationType findByOperationType(String operationType);

		List<SystemConfigOperationType> findByStatus(String string);

	}

	public interface SystemConfigErrorDefinationRepository extends JpaRepository<SystemConfigErrorDefination, Long> {

		List<SystemConfigErrorDefination> findByAuthStatus(String string);

		SystemConfigErrorDefination findByErrorCode(String errorCode);

		Page<SystemConfigErrorDefination> findAll(Specification<SystemConfigErrorDefination> specification,
				Pageable paging);

	}

	public interface SystemConfigOperationRepository extends JpaRepository<SystemConfigOperation, Long> {

		SystemConfigOperation findByOperationTypeAndCode(String operationType, String code);

		SystemConfigOperation findBySystemConfigOperationId(Long operationId);

		Page<SystemConfigOperation> findAll(Specification<SystemConfigOperation> specification, Pageable paging);

		List<SystemConfigOperation> findByAuthStatus(String string);

		List<SystemConfigOperation> findByStatus(String string);

	}

}
