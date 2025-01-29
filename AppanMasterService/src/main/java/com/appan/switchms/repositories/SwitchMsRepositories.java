package com.appan.switchms.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appan.entity.SwitchOperatorMaster;
import com.appan.entity.SwitchTypeMaster;

@Repository
public class SwitchMsRepositories {

	public interface SwitchTypeMasterRepository extends JpaRepository<SwitchTypeMaster, Long> {

		SwitchTypeMaster findBySwitchType(String switchType);

		Page<SwitchTypeMaster> findAll(Specification<SwitchTypeMaster> specification, Pageable paging);

		SwitchTypeMaster findBySwitchTypeAndCode(String switchType, String code);

		List<SwitchTypeMaster> findByStatus(String string);

	}

	public interface SwitchOperatorMasterRepository extends JpaRepository<SwitchOperatorMaster, Long> {

		Page<SwitchOperatorMaster> findAll(Specification<SwitchOperatorMaster> specification, Pageable paging);

		SwitchOperatorMaster findByUserNameAndOperatorName(String userName, String operatorName);

		List<SwitchOperatorMaster> findByStatus(String string);
	}
}
