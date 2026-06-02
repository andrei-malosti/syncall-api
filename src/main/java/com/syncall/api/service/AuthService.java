package com.syncall.api.service;

import com.syncall.api.dto.auth.AuthLoginRequestDTO;
import com.syncall.api.dto.auth.AuthRegisterRequestDTO;
import com.syncall.api.dto.auth.AuthResponseDTO;
import com.syncall.api.dto.company.CompanyRequestDTO;
import com.syncall.api.exception.BusinessException;
import com.syncall.api.infra.security.TokenService;
import com.syncall.api.infra.security.userdetails.CustomUserDetails;
import com.syncall.api.model.Role;
import com.syncall.api.model.entity.Company;
import com.syncall.api.model.entity.User;
import com.syncall.api.repository.CompanyRepository;
import com.syncall.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDTO register(AuthRegisterRequestDTO request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BusinessException("Email já está em uso");
        }

        if(companyRepository.existsByCnpj(request.getCompany().getCnpj())){
            throw new BusinessException("Empresa já cadastrada");
        }

        var company = buildCompany(request.getCompany());
        company = companyRepository.save(company);

        var user = buildUser(request, company);
        user = userRepository.save(user);


        return AuthResponseDTO.builder()
                .token(generateToken(user.getEmail(), request.getPassword()))
                .build();
    }

    public AuthResponseDTO login(AuthLoginRequestDTO loginRequest){
        try{
            String token = generateToken(loginRequest.getEmail(), loginRequest.getPassword());
            return AuthResponseDTO.builder()
                    .token(token)
                    .build();
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciais invalidas");
        }
    }

    private String generateToken(String email, String password){
            var userAuth = new UsernamePasswordAuthenticationToken(email, password);
            var authentication = authenticationManager.authenticate(userAuth);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            if (userDetails == null) {
                throw new RuntimeException("Autenticação falhou");
            }
            return tokenService.generateToken(userDetails);
    }

    private User buildUser(AuthRegisterRequestDTO request, Company company){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MANAGER)
                .company(company)
                .build();
    }

    private Company buildCompany(CompanyRequestDTO companyRequest){
        return Company.builder()
                .name(companyRequest.getName())
                .cnpj(companyRequest.getCnpj())
                .build();
    }

}
