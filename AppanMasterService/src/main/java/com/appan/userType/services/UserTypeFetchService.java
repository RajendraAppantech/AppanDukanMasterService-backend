package com.appan.userType.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.entity.UserTypeMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserTypeMasterRepository;
import com.appan.userType.models.UserTypeFetchRequest;
import com.appan.userType.models.UserTypeFetchResponse;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UserTypeFetchService {

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private UserTypeMasterRepository userTypeMasterRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserTypeFetchService.class);

    public UserTypeFetchResponse fetch(UserTypeFetchRequest req, Integer pageNo, Integer pageSize) {
        try {
            UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
            if (user == null) {
                return buildResponse(false, "Invalid Username", "01", null, null);
            }

            Pageable paging = PageRequest.of(pageNo - 1, pageSize);

            Specification<UserTypeMaster> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
                query.orderBy(criteriaBuilder.desc(root.get("createdDate")));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            Page<UserTypeMaster> pageResults = userTypeMasterRepository.findAll(specification, paging);

            if (pageResults.isEmpty()) {
                return buildResponse(false, "No UserType found.", "01", null, null);
            }

            PageDetails pageDetails = new PageDetails();
            pageDetails.setPageNo(pageResults.getNumber() + 1);
            pageDetails.setTotalRecords(pageResults.getTotalElements());
            pageDetails.setNoOfPages(pageResults.getTotalPages());
            pageDetails.setPageSize(pageResults.getSize());

            List<UserTypeMaster> userTypeData = pageResults.stream().collect(Collectors.toList());
            return buildResponse(true, "SUCCESS", "00", userTypeData, pageDetails);

        } catch (Exception e) {
            logger.error("Exception occurred while fetching user type data: ", e);
            return buildResponse(false, "EXCEPTION", "03", null, null);
        }
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<UserTypeMaster> root, UserTypeFetchRequest req) {
        List<Predicate> predicates = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        if (req.getFromDate() != null && req.getToDate() != null) {
            try {
                Date startDate = dateFormat.parse(req.getFromDate());
                Date endDate = dateFormat.parse(req.getToDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(endDate);
                cal.add(Calendar.DAY_OF_YEAR, 1);
                endDate = cal.getTime();
                predicates.add(cb.between(root.get("createdDate"), startDate, endDate));
            } catch (ParseException e) {
                logger.error("Error parsing date filters", e);
            }
        }

        if (!Strings.isNullOrEmpty(req.getUserType()) && !"all".equalsIgnoreCase(req.getUserType())) {
            predicates.add(cb.equal(cb.lower(root.get("userType")), req.getUserType().toLowerCase()));
        }
        if (!Strings.isNullOrEmpty(req.getUserCode()) && !"all".equalsIgnoreCase(req.getUserCode())) {
            predicates.add(cb.equal(cb.lower(root.get("userCode")), req.getUserCode().toLowerCase()));
        }
        if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
            predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
        }

        return predicates;
    }

    private UserTypeFetchResponse buildResponse(boolean status, String message, String respCode,
                                                List<UserTypeMaster> data, PageDetails pageDetails) {
        UserTypeFetchResponse response = new UserTypeFetchResponse();
        response.setStatus(status);
        response.setMessage(message);
        response.setRespCode(respCode);
        response.setData(data);
        response.setPageDetails(pageDetails);
        return response;
    }

    public UserTypeFetchResponse getAllData() {
        try {
            List<UserTypeMaster> allData = userTypeMasterRepository.findByStatus("Active");
            if (allData.isEmpty()) {
                return buildResponse(false, "No data found", "01", null, null);
            }
            return buildResponse(true, "SUCCESS", "00", allData, null);
        } catch (Exception e) {
            logger.error("Exception occurred while fetching all user type data: ", e);
            return buildResponse(false, "EXCEPTION", "03", null, null);
        }
    }
}
