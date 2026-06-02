package com.syncall.api.service;

import com.syncall.api.dto.ticket.TicketRegisterRequestDTO;
import com.syncall.api.dto.ticket.TicketResponseDTO;
import com.syncall.api.exception.BusinessException;
import com.syncall.api.exception.ResourceNotFoundException;
import com.syncall.api.model.TicketStatus;
import com.syncall.api.model.entity.Company;
import com.syncall.api.model.entity.Ticket;
import com.syncall.api.repository.CompanyRepository;
import com.syncall.api.repository.TicketRepository;
import com.syncall.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ChatService chatService;
    private final ReportService reportService;

    @Transactional
    public TicketResponseDTO create(TicketRegisterRequestDTO register, Long clientId, Long companyId){
        var client = userRepository.getReferenceById(clientId);
        var company = companyRepository.getReferenceById(companyId);

        if(ticketRepository.countClientTickets(clientId) >= 1)
            throw new BusinessException("Você só pode ter um chamado por vez, aguarde seu chamado ser concluido");

        var ticket = buildTicket(register, company);
        ticket.addUser(client);

        return TicketResponseDTO.from(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponseDTO assignAttendantToTicket(Long ticketId, Long attendantId, Long companyId){
        var attendant = userRepository.getReferenceById(attendantId);

        //trava para corrida entre atendentes clicando ao mesmo tempo, muda o status do ticket para in_progress se ele estiver open
        if(ticketRepository.lockTicket(ticketId) == 0)
            throw new BusinessException("Chamado ja foi atendido!");

        var ticket = ticketRepository.findByIdAndCompanyId(ticketId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado"));
        var company = companyRepository.getReferenceById(companyId);

        chatService.buildAndSaveChat(ticket, company);

        ticket.addUser(attendant);
        return TicketResponseDTO.from(ticket);
    }

    @Transactional
    public TicketResponseDTO concludeTicket(Long clientId, Long companyId){
        var ticket = ticketRepository.findClientTicket(clientId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado ou inexistente"));
        if(ticket.getStatus() == TicketStatus.OPEN)
            throw new BusinessException("Não é possivel concluir um chamado que não está em progresso");
        ticket.setStatus(TicketStatus.CONCLUDED);
        ticket.setConcludedAt(LocalDateTime.now());
        var company = companyRepository.getReferenceById(companyId);
        reportService.buildAndSaveReport(ticket, company);
        return TicketResponseDTO.from(ticket);
    }

    public Slice<TicketResponseDTO> findAllTickets(Long companyId, Pageable pageable){
        return ticketRepository.findAllTickets(companyId, pageable)
                .map(TicketResponseDTO::from);
    }

    public TicketResponseDTO findClientTicket(Long clientId, Long companyId){
        var ticket = ticketRepository.findClientTicket(clientId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado ou inexistente"));
        return TicketResponseDTO.from(ticket);
    }

    public TicketResponseDTO findTicket(Long ticketId, Long companyId){
        var ticket = ticketRepository.findTicket(ticketId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado"));
        return TicketResponseDTO.from(ticket);
    }

    public Slice<TicketResponseDTO> findAttendantTicketsInProgress(Long attendantId, Long companyId, Pageable pageable){
        return ticketRepository.findAttendantTicketsInProgress(attendantId, companyId, pageable)
                .map(TicketResponseDTO::from);
    }

    private Ticket buildTicket(TicketRegisterRequestDTO register, Company company){
        return Ticket.builder()
                .description(register.getDescription())
                .createdAt(LocalDateTime.now())
                .status(TicketStatus.OPEN)
                .users(new ArrayList<>())
                .company(company)
                .build();
    }

}
