package com.syncall.api.repository;

import com.syncall.api.model.entity.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT COUNT(t) FROM Ticket t JOIN t.users u WHERE u.id = :clientId AND t.status IN ('OPEN', 'IN_PROGRESS')")
    Long countClientTickets(@Param("clientId") Long clientId);

    @Query("SELECT t FROM Ticket t WHERE t.id = :id AND t.company.id = :companyId")
    Optional<Ticket> findByIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);

    @Modifying
    @Query("UPDATE Ticket t SET t.status = 'IN_PROGRESS' WHERE t.id = :id AND t.status = 'OPEN'")
    int lockTicket(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t WHERE t.company.id = :companyId AND t.status = 'OPEN'")
    Slice<Ticket> findAllTickets(@Param("companyId") Long companyId, Pageable pageable);

    @Query("SELECT t FROM Ticket t JOIN t.users u WHERE u.id = :clientId AND t.company.id = :companyId AND (t.status = 'OPEN' OR t.status = 'IN_PROGRESS')")
    Optional<Ticket> findClientTicket(@Param("clientId") Long clientId, @Param("companyId") Long companyId);

    @Query("SELECT t FROM Ticket t JOIN t.users u WHERE u.id = :attendantId AND t.company.id = :companyId AND t.status = 'IN_PROGRESS'")
    Slice<Ticket> findAttendantTicketsInProgress(@Param("attendantId") Long attendantId, @Param("companyId") Long companyId, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.id = :id AND t.company.id = :companyId")
    Optional<Ticket> findTicket(@Param("id") Long id, @Param("companyId") Long companyId);
}
