package com.appan.planmaster.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appan.planmaster.entity.CircleSeriesMaster;
import com.appan.planmaster.entity.CirclesMaster;
import com.appan.planmaster.entity.RechargePlansMaster;

@Repository
public class PlanMasterRepositories {

	public interface CirclesMasterRepository extends JpaRepository<CirclesMaster, Long> {

		CirclesMaster findByCircleCode1OrCircleCode2OrCircleCode3(String trim, String trim2, String trim3);

		Page<CirclesMaster> findAll(Specification<CirclesMaster> specification, Pageable paging);

		CirclesMaster findByCircleNameOrCode(String circleName, String code);

		List<CirclesMaster> findByStatus(String string);
	}

	public interface CircleSeriesMasterRepository extends JpaRepository<CircleSeriesMaster, Long> {

		CircleSeriesMaster findByNumberPrefixAndCircleNameAndOperatorName(String trim, String trim2, String trim3);

		Page<CircleSeriesMaster> findAll(Specification<CircleSeriesMaster> specification, Pageable paging);

		List<CircleSeriesMaster> findByStatus(String string);
	}

	public interface RechargePlansMasterRepository extends JpaRepository<RechargePlansMaster, Long> {

		RechargePlansMaster findByPlanIdAndCircleNameAndOperatorName(String trim, String trim2, String trim3);

		Page<RechargePlansMaster> findAll(Specification<RechargePlansMaster> specification, Pageable paging);

		List<RechargePlansMaster> findByStatus(String string);
	}

}
