package com.syncall.api.repository;

import com.syncall.api.model.entity.Message;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.company.id = :companyId")
    Slice<Message> findChatMessages(@Param("chatId") Long chatId, @Param("companyId") Long companyId);

}
