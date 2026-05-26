package com.syncall.api.dto.company;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequestDTO {

    @NotBlank(message = "Nome da empresa é obrigatorio")
    private String name;

    @NotBlank(message = "CNPJ é obrigatorio")
    private String cnpj;
}
