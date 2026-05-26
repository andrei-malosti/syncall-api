package com.syncall.api.service;

import com.syncall.api.dto.user.UserRegisterRequestDTO;
import com.syncall.api.dto.user.UserResponseDTO;
import com.syncall.api.exception.BusinessException;
import com.syncall.api.exception.ResourceNotFoundException;
import com.syncall.api.model.Role;
import com.syncall.api.model.entity.Company;
import com.syncall.api.model.entity.User;
import com.syncall.api.repository.CompanyRepository;
import com.syncall.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO createAttendant(UserRegisterRequestDTO register, Long companyId){
        if(userRepository.existsByEmail(register.getEmail()))
            throw new BusinessException("Email já em uso");

        if(!companyRepository.existsById(companyId))
            throw new ResourceNotFoundException("Empresa não encontrada");

        var attendant = buildAttendant(register, companyRepository.getReferenceById(companyId));

        return UserResponseDTO.from(userRepository.save((attendant)));
    }

    private User buildAttendant(UserRegisterRequestDTO register, Company company){
        return User.builder()
                .name(register.getName())
                .email(register.getEmail())
                .password(passwordEncoder.encode(register.getPassword()))
                .role(Role.ATTENDANT)
                .company(company)
                .build();
    }
}
