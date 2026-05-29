package com.syncall.api.controller;

import com.syncall.api.infra.multitenancy.CompanyContext;
import com.syncall.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<?> findCompanyReports(@PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(reportService.findCompanyReports(CompanyContext.getCompanyId(), pageable));
    }
}
