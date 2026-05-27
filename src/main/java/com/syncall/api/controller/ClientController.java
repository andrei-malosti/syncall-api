package com.syncall.api.controller;

import com.syncall.api.dto.user.UserRegisterRequestDTO;
import com.syncall.api.infra.multitenancy.CompanyContext;
import com.syncall.api.service.UserService;
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
@RequestMapping("/clients")
public class ClientController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRegisterRequestDTO registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createClient(registerRequest, CompanyContext.getCompanyId()));
    }

    @GetMapping
    public ResponseEntity<?> findAllClients(
            @PageableDefault(page = 0, size = 20 , sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable){
                return ResponseEntity.ok(userService.findAllClients(pageable, CompanyContext.getCompanyId()));
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<?> findClient(@PathVariable Long clientId){
                return ResponseEntity.ok(userService.findClient(clientId));
    }

}
