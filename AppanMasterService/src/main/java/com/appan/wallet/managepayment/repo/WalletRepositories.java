package com.appan.wallet.managepayment.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appan.entity.WalletBulkDebitCredit;
import com.appan.entity.WalletDebitMaster;
import com.appan.entity.WalletPaymentMaster;

@Repository
public class WalletRepositories {

	public interface WalletPaymentMasterRepository extends JpaRepository<WalletPaymentMaster, Long> {

		Page<WalletPaymentMaster> findAll(Specification<WalletPaymentMaster> specification, Pageable paging);

		List<WalletPaymentMaster> findByAuthStatus(String string);
	}

	public interface WalletDebitMasterRepository extends JpaRepository<WalletDebitMaster, Long> {

		Page<WalletDebitMaster> findAll(Specification<WalletDebitMaster> specification, Pageable paging);

		List<WalletDebitMaster> findByAuthStatus(String string);
	}

	public interface WalletBulkDebitCreditRepository extends JpaRepository<WalletBulkDebitCredit, Long> {

		Page<WalletBulkDebitCredit> findAll(Specification<WalletBulkDebitCredit> specification,  Pageable paging);

	}

}
