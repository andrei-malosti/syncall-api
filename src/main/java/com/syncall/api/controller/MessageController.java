package com.syncall.api.controller;

import com.syncall.api.dto.message.MessageRegisterRequestDTO;
import com.syncall.api.infra.multitenancy.CompanyContext;
import com.syncall.api.infra.multitenancy.UserContext;
import com.syncall.api.service.MessageService;
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
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<?> create(@Valid @RequestBody MessageRegisterRequestDTO register, @PathVariable Long chatId){
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(register, chatId, CompanyContext.getCompanyId(), UserContext.getUserId()));
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> findChatMessages(
            @PathVariable Long chatId,
            @PageableDefault(page = 0, size = 20, sort="sendAt", direction = Sort.Direction.ASC)
            Pageable pageble){
        return ResponseEntity.ok(messageService.findChatMessages(chatId, CompanyContext.getCompanyId(), pageble));
    }

}
