package com.syncall.api.service;

import com.syncall.api.dto.report.ReportResponseDTO;
import com.syncall.api.model.entity.Company;
import com.syncall.api.model.entity.Report;
import com.syncall.api.model.entity.Ticket;
import com.syncall.api.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Slice<ReportResponseDTO> findCompanyReports(Long companyId, Pageable pageable){
        return reportRepository.findCompanyReports(companyId, pageable)
                .map(ReportResponseDTO::from);
    }

    public void buildAndSaveReport(Ticket ticket, Company company){
        var report = Report.builder()
                .ticket(ticket)
                .company(company)
                .build();
        reportRepository.save(report);
    }
}
