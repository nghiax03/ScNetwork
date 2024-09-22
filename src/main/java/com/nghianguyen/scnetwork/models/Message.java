package com.nghianguyen.scnetwork.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    private String content;

    @Column(name = "sent_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

}
