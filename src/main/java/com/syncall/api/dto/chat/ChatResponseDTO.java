package com.syncall.api.dto.chat;

import com.syncall.api.model.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDTO {

    private Long id;
    private Long ticketId;
    private Long companyId;

    public static ChatResponseDTO from(Chat chat){
        return ChatResponseDTO.builder()
                .id(chat.getId())
                .ticketId(chat.getTicket().getId())
                .companyId(chat.getCompany().getId())
                .build();
    }

}
