package com.syncall.api.dto.message;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRegisterRequestDTO {

    @NotBlank(message = "Uma mensagem deve conter caracteres")
    private String content;

}
