package com.syncall.api.controller;

import com.syncall.api.dto.ticket.TicketRegisterRequestDTO;
import com.syncall.api.infra.multitenancy.CompanyContext;
import com.syncall.api.infra.multitenancy.UserContext;
import com.syncall.api.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TicketRegisterRequestDTO registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.create(registerRequest, UserContext.getUserId(), CompanyContext.getCompanyId()));
    }

    @PatchMapping("/{ticketId}")
    public ResponseEntity<?> assignTicket(@PathVariable Long ticketId){
        return ResponseEntity.ok(ticketService.assignAttendantToTicket(ticketId, UserContext.getUserId(), CompanyContext.getCompanyId()));
    }

    @GetMapping
    public ResponseEntity<?> findAllCompanyOpenTickets(
            @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable){
        return ResponseEntity.ok(ticketService.findAllTickets(CompanyContext.getCompanyId(), pageable));
    }

    @GetMapping("/client")
    public ResponseEntity<?> findClientTicket(){
        return ResponseEntity.ok(ticketService.findClientTicket(UserContext.getUserId(), CompanyContext.getCompanyId()));
    }

    @GetMapping("/attendant")
    public ResponseEntity<?> findAttendantInProgressTicket(
            @PageableDefault(page =  0, size = 20, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable){
        return ResponseEntity.ok(ticketService.findAttendantTicketsInProgress(UserContext.getUserId(), CompanyContext.getCompanyId(), pageable));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<?> findTicket(@PathVariable Long ticketId){
        return ResponseEntity.ok(ticketService.findTicket(ticketId, CompanyContext.getCompanyId()));
    }

}
