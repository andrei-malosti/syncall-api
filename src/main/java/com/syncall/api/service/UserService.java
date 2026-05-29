package com.syncall.api.service;

import com.syncall.api.dto.user.UserRegisterRequestDTO;
import com.syncall.api.dto.user.UserResponseDTO;
import com.syncall.api.exception.BusinessException;
import com.syncall.api.exception.ResourceNotFoundException;
import com.syncall.api.infra.multitenancy.CompanyContext;
import com.syncall.api.model.AttendantStatus;
import com.syncall.api.model.PasswordGenerator;
import com.syncall.api.model.Role;
import com.syncall.api.model.entity.Company;
import com.syncall.api.model.entity.User;
import com.syncall.api.repository.CompanyRepository;
import com.syncall.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public UserResponseDTO createAttendant(UserRegisterRequestDTO register, Long companyId){
        if(userRepository.existsByEmail(register.getEmail()))
            throw new BusinessException("Email já em uso");

        if(!companyRepository.existsById(companyId))
            throw new ResourceNotFoundException("Empresa não encontrada");

        var randomPassword = PasswordGenerator.generate(8);

        var attendant = buildAttendant(register, companyRepository.getReferenceById(companyId), randomPassword);
        attendant = userRepository.save(attendant);
        emailService.sendFirstTimeEmail(attendant.getName(), attendant.getEmail(), randomPassword);

        return UserResponseDTO.from(attendant);
    }

    public Slice<UserResponseDTO> findAllAttendants(Pageable pageable, Long companyId){
        return userRepository.findAllAttendants(pageable, companyId)
                .map(UserResponseDTO::from);
    }

    public UserResponseDTO findAttendant(Long attendantId, Long companyId){
        var user = userRepository.findAttendantById(attendantId, companyId).orElseThrow(() -> new ResourceNotFoundException("Atendente não encontrado"));
        return UserResponseDTO.from(user);
    }

    public UserResponseDTO createClient(UserRegisterRequestDTO register, Long companyId){
        if(userRepository.existsByEmail(register.getEmail()))
            throw new BusinessException("Email já em uso");

        if(!companyRepository.existsById(companyId))
            throw new BusinessException("Empresa não encontrada");

        var randomPassword = PasswordGenerator.generate(8);

        var client = buildClient(register, companyRepository.getReferenceById(companyId), randomPassword);
        client = userRepository.save(client);
        emailService.sendFirstTimeEmail(client.getName(), client.getEmail(), randomPassword);

        return UserResponseDTO.from(client);
    }

    public Slice<UserResponseDTO> findAllClients(Pageable pageable, Long companyId){
        return userRepository.findAllClients(pageable, companyId)
                .map(UserResponseDTO::from);
    }

    public UserResponseDTO findClient(Long clientId){
        var user = userRepository.findClientById(clientId, CompanyContext.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return UserResponseDTO.from(user);
    }

    private User buildAttendant(UserRegisterRequestDTO register, Company company, String password){
        return User.builder()
                .name(register.getName())
                .email(register.getEmail())
                .password(passwordEncoder.encode(password))
                .role(Role.ATTENDANT)
                .company(company)
                .availabilityStatus(AttendantStatus.AVAILABLE)
                .build();
    }

    private User buildClient(UserRegisterRequestDTO register, Company company, String password){
        return User.builder()
                .name(register.getName())
                .email(register.getEmail())
                .password(passwordEncoder.encode(password))
                .role(Role.CLIENT)
                .company(company)
                .build();
    }
}
