package com.syncall.api.dto.report;

import com.syncall.api.model.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTO {

    private Long id;
    private String ticketDescription;
    private LocalDateTime ticketCreatedAt;
    private LocalDateTime ticketConcludedAt;

    public static ReportResponseDTO from(Report report){
        return ReportResponseDTO.builder()
                .id(report.getId())
                .ticketDescription(report.getTicket().getDescription())
                .ticketCreatedAt(report.getTicket().getCreatedAt())
                .ticketConcludedAt(report.getTicket().getConcludedAt())
                .build();
    }

}
