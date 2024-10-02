package com.nghianguyen.scnetwork.websocket;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;
}
