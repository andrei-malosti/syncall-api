package com.syncall.api.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketRegisterRequestDTO {

    @NotBlank(message = "Descrição do problema é necessária")
    private String description;

}
