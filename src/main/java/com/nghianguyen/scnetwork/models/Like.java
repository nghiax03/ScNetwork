package com.nghianguyen.scnetwork.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, updatable = false)
    private Long id;

    private Long count = 0L;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;


}
