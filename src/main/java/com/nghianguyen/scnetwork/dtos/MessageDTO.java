package com.nghianguyen.scnetwork.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDTO {
    @JsonProperty("sender_id")
    Long senderId;

    @JsonProperty("receiver_id")
    Long receiverId;
    String content;
}
