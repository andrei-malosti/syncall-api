package com.syncall.api.model.entity;

import com.syncall.api.model.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime concludedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @ManyToMany
    @JoinTable(name = "ticket_user",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private List<User> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    private Company company;

    public void addUser(User user){
        this.users.add(user);
        user.getTickets().add(this);
    }
}
