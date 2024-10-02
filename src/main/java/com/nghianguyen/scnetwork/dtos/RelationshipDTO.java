package com.nghianguyen.scnetwork.dtos;

import com.nghianguyen.scnetwork.models.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipDTO {
    private String id;
    private User userOne;
    private User userTwo;
    private String status;
    private User actionUser;
    private LocalDateTime time;
}
