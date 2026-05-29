package com.syncall.api.dto.auth;

import com.syncall.api.dto.company.CompanyRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterRequestDTO {

    @Email(message = "Deve ser um email valido")
    @NotBlank(message = "Email do usuario é obrigatorio")
    private String email;

    @NotBlank(message = "Nome do usuario é obrigatorio")
    private String name;

    @NotBlank(message = "Senha é obrigatoria")
    @Size(min = 6, message = "A senha deve conter no minimo 6 caracteres")
    private String password;

    @Valid
    private CompanyRequestDTO company;

}
