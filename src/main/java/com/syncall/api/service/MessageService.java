package com.syncall.api.service;

import com.syncall.api.dto.message.MessageRegisterRequestDTO;
import com.syncall.api.dto.message.MessageResponseDTO;
import com.syncall.api.exception.BusinessException;
import com.syncall.api.model.entity.Chat;
import com.syncall.api.model.entity.Company;
import com.syncall.api.model.entity.Message;
import com.syncall.api.model.entity.User;
import com.syncall.api.repository.ChatRepository;
import com.syncall.api.repository.CompanyRepository;
import com.syncall.api.repository.MessageRepository;
import com.syncall.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ChatRepository chatRepository;

    // ver o que fazer em relação a proteção das mensagens quanto a usuarios de mesma empresa tentando criar mensagens no chat de um chamado que não é dele
    @Transactional
    public MessageResponseDTO create(MessageRegisterRequestDTO register, Long chatId, Long companyId, Long userId){
        if(!chatRepository.existsByIdAndCompanyId(chatId, companyId))
            throw new BusinessException("Chat não encontrado, sua mensagem não foi enviada");

        var user = userRepository.getReferenceById(userId);
        var company = companyRepository.getReferenceById(companyId);
        var chat = chatRepository.getReferenceById(chatId);

        var message = buildMessage(register, user, company, chat);
        message = messageRepository.save(message);
        return MessageResponseDTO.from(message);
    }

    // mesmo que o create, pensar em adicionar os usuarios dono do chat para evitar usuario que nao pertence ao chat ver as mensagens dele
    public Slice<MessageResponseDTO> findChatMessages(Long chatId, Long companyId, Pageable pageable){
        return messageRepository.findChatMessages(chatId, companyId)
                .map(MessageResponseDTO::from);
    }

    private Message buildMessage(MessageRegisterRequestDTO registerRequest, User user, Company company, Chat chat){
        return Message.builder()
                .content(registerRequest.getContent())
                .user(user)
                .company(company)
                .chat(chat)
                .sendAt(LocalDateTime.now())
                .build();
    }

}
