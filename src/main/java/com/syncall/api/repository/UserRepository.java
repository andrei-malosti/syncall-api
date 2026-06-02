package com.syncall.api.repository;

import com.syncall.api.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = 'ATTENDANT' AND u.company.id = :companyId")
    Slice<User> findAllAttendants(Pageable pageable, @Param("companyId") Long companyId);

    @Query("SELECT u FROM User u WHERE u.role = 'ATTENDANT' " +
            "AND u.id = :attendantId AND u.company.id = :companyId")
    Optional<User> findAttendantById(@Param("attendantId") Long attendantId, @Param("companyId") Long companyId);

    @Query("SELECT u FROM User u WHERE u.role = 'CLIENT' AND u.company.id = :companyId")
    Slice<User> findAllClients(Pageable pageable, @Param("companyId") Long companyId);

    @Query("SELECT u FROM User u WHERE u.role = 'CLIENT' " +
            "AND u.id = :clientId AND u.company.id = :companyId")
    Optional<User> findClientById(Long clientId, Long companyId);

}
