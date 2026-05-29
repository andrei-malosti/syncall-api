package com.syncall.api.service;

import com.syncall.api.dto.chat.ChatResponseDTO;
import com.syncall.api.exception.ResourceNotFoundException;
import com.syncall.api.model.entity.Chat;
import com.syncall.api.model.entity.Company;
import com.syncall.api.model.entity.Ticket;
import com.syncall.api.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatResponseDTO findTicketChat(Long ticketId, Long companyId){
        var chat = chatRepository.findChatByTicketId(ticketId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat não encontrado"));
        return ChatResponseDTO.from(chat);
    }

    public Chat buildAndSaveChat(Ticket ticket, Company company){
        var chat = Chat.builder()
                .company(company)
                .ticket(ticket)
                .build();

        chat = chatRepository.save(chat);
        return chat;
    }

}
