package com.syncall.api.repository;

import com.syncall.api.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.ticket.id = :ticketId AND c.company.id = :companyId")
    Optional<Chat> findChatByTicketId(@Param("ticketId") Long ticketId, @Param("companyId") Long companyId);

    Boolean existsByIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);

}
