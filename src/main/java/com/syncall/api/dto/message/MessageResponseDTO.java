package com.syncall.api.dto.message;

import com.syncall.api.model.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {

    private Long id;
    private String content;
    private LocalDateTime sendAt;
    private String userName;
    private Long userId;
    private Long chatId;

    public static MessageResponseDTO from(Message message){
        return MessageResponseDTO.builder()
                .id(message.getId())
                .content(message.getContent())
                .sendAt(message.getSendAt())
                .userName(message.getUser().getName())
                .userId(message.getUser().getId())
                .chatId(message.getChat().getId())
                .build();
    }

}
