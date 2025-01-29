package com.appan.kycmaster.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appan.kycmaster.entity.DocumentAttributeMaster;
import com.appan.kycmaster.entity.DocumentMaster;
import com.appan.kycmaster.entity.EkycMaster;
import com.appan.kycmaster.entity.KycGroupConfigMaster;
import com.appan.kycmaster.entity.KycGroupMaster;

@Repository
public class KycRepositories {

	public interface EkycMasterRepository extends JpaRepository<EkycMaster, Long> {

		boolean existsByCodeIgnoreCase(String ekycCode);

		EkycMaster findByEkycNameIgnoreCaseOrCodeIgnoreCase(String ekycName, String code);

		Page<EkycMaster> findAll(Specification<EkycMaster> specification, Pageable paging);

		List<EkycMaster> findByStatus(String string);
	}

	public interface DocumentMasterRepository extends JpaRepository<DocumentMaster, Long> {

		boolean existsByDocumentNameIgnoreCase(String documentName);

		DocumentMaster findByDocumentNameIgnoreCase(String documentName);

		List<DocumentMaster> findByAuthStatus(String string);

		Page<DocumentMaster> findAll(Specification<DocumentMaster> specification, Pageable paging);

		List<DocumentMaster> findByStatus(String string);
	}

	public interface DocumentAttributeMasterRepository extends JpaRepository<DocumentAttributeMaster, Long> {

		DocumentAttributeMaster findByAttributeNameIgnoreCase(String attributeName);

		Page<DocumentAttributeMaster> findAll(Specification<DocumentAttributeMaster> specification, Pageable paging);

		List<DocumentAttributeMaster> findByStatus(String string);
	}

	public interface KycGroupMasterRepository extends JpaRepository<KycGroupMaster, Long> {

		boolean existsByCodeIgnoreCase(String groupCode);

		KycGroupMaster findByKycGroupNameOrCodeIgnoreCase(String kycGroupName, String code);

		Page<KycGroupMaster> findAll(Specification<KycGroupMaster> specification, Pageable paging);

		List<KycGroupMaster> findByStatus(String string);
	}

	public interface KycGroupConfigMasterRepository extends JpaRepository<KycGroupConfigMaster, Long> {

		boolean existsByDocumentNameIgnoreCaseAndDocumentGroupNameIgnoreCase(String documentName,
				String documentGroupName);

		KycGroupConfigMaster findByDocumentNameOrDocumentGroupNameIgnoreCase(String documentName,
				String documentGroupName);

		List<KycGroupConfigMaster> findByAuthStatus(String string);

		Page<KycGroupConfigMaster> findAll(Specification<KycGroupConfigMaster> specification, Pageable paging);

		List<KycGroupConfigMaster> findByStatus(String string);

	}

}
