package com.syncall.api.repository;

import com.syncall.api.model.entity.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r WHERE r.company.id = :companyId")
    Slice<Report> findCompanyReports(@Param("companyId") Long companyId, Pageable pageable);
}
