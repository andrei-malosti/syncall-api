package com.syncall.api.controller;

import com.syncall.api.infra.multitenancy.CompanyContext;
import com.syncall.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<?> findTicketChat(@PathVariable Long ticketId){
        return ResponseEntity.ok(chatService.findTicketChat(ticketId, CompanyContext.getCompanyId()));
    }
}
