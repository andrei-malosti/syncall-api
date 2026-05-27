package com.syncall.api.dto.ticket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.syncall.api.model.Role;
import com.syncall.api.model.TicketStatus;
import com.syncall.api.model.entity.Ticket;
import com.syncall.api.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResponseDTO {

    private Long id;
    private String description;
    private LocalDateTime createdAt;
    private TicketStatus ticketStatus;
    private String attendantName;
    private Long attendantId;
    private String clientName;
    private Long clientId;

    public static TicketResponseDTO from(Ticket ticket){
        var users = ticket.getUsers();
        User attendant = users.stream()
                .filter(u -> u.getRole() == Role.ATTENDANT)
                .findFirst()
                .orElse(null);

        User client = users.stream()
                .filter(u -> u.getRole() == Role.CLIENT)
                .findFirst()
                .orElse(null);

        var ticketBuilder = TicketResponseDTO.builder()
                .id(ticket.getId())
                .description(ticket.getDescription())
                .createdAt(ticket.getCreatedAt())
                .ticketStatus(ticket.getStatus());

        if(attendant != null){
            ticketBuilder
                    .attendantName(attendant.getName())
                    .attendantId(attendant.getId());
        }
        if(client != null){
            ticketBuilder
                    .clientName(client.getName())
                    .clientId(client.getId());
        }

        return ticketBuilder.build();
    }

}
