package com.nghianguyen.scnetwork.models.relationship;

import com.nghianguyen.scnetwork.models.Message;
import com.nghianguyen.scnetwork.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "relationship")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "user_one_id", referencedColumnName = "id")
    private User userOne;

    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "user_two_id", referencedColumnName = "id")
    private User userTwo;

    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "action_user_id", referencedColumnName = "id")
    private User actionUser;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

//    @OneToMany(mappedBy = "relationship", targetEntity = Message.class, cascade = CascadeType.ALL)
//    private List<Message> messageList; //message in model
}
