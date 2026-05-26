package com.syncall.api.controller;

import com.syncall.api.dto.user.UserRegisterRequestDTO;
import com.syncall.api.infra.multitenancy.CompanyContext;
import com.syncall.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendant")
public class AttendantController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> create(@Valid @RequestBody UserRegisterRequestDTO registerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createAttendant(registerRequest, CompanyContext.getCompanyId()));
    }

}
